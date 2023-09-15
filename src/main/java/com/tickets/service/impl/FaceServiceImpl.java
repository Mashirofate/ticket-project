package com.tickets.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.arcsoft.face.*;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.toolkit.ImageInfo;
import com.tickets.factory.FaceEngineFactory;
import com.tickets.mapper.FaceMapper;
import com.tickets.rpc.BusinessException;
import com.tickets.rpc.ErrorCodeEnum;
import com.tickets.service.FaceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class FaceServiceImpl implements FaceService {
    @Resource
    private FaceMapper faceMapper;




    public final static Logger logger = LoggerFactory.getLogger(FaceServiceImpl.class);

    @Value("${config.arcface-sdk.sdk-lib-path}")
    public String sdkLibPath;

    @Value("${config.arcface-sdk.app-id}")
    public String appId;

    @Value("${config.arcface-sdk.sdk-key}")
    public String sdkKey;

    @Value("${config.arcface-sdk.detect-pool-size}")
    public Integer detectPooSize;

    @Value("${config.arcface-sdk.compare-pool-size}")
    public Integer comparePooSize;

    private ExecutorService compareExecutorService;

    //通用人脸识别引擎池
    private GenericObjectPool<FaceEngine> faceEngineGeneralPool;

    //人脸比对引擎池
    private GenericObjectPool<FaceEngine> faceEngineComparePool;


    @PostConstruct
    public void init() {


        GenericObjectPoolConfig detectPoolConfig = new GenericObjectPoolConfig();
        detectPoolConfig.setMaxIdle(detectPooSize);
        detectPoolConfig.setMaxTotal(detectPooSize);
        detectPoolConfig.setMinIdle(detectPooSize);
        detectPoolConfig.setLifo(false);
        EngineConfiguration detectCfg = new EngineConfiguration();
        FunctionConfiguration detectFunctionCfg = new FunctionConfiguration();
        detectFunctionCfg.setSupportFaceDetect(true);//开启人脸检测功能
        detectFunctionCfg.setSupportFaceRecognition(true);//开启人脸识别功能
        detectFunctionCfg.setSupportAge(true);//开启年龄检测功能
        detectFunctionCfg.setSupportGender(true);//开启性别检测功能
        detectFunctionCfg.setSupportLiveness(true);//开启活体检测功能
        detectCfg.setFunctionConfiguration(detectFunctionCfg);
        detectCfg.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);//图片检测模式，如果是连续帧的视频流图片，那么改成VIDEO模式
        detectCfg.setDetectFaceOrientPriority(DetectOrient.ASF_OP_0_ONLY);//人脸旋转角度
        faceEngineGeneralPool = new GenericObjectPool(new FaceEngineFactory(sdkLibPath, appId, sdkKey, null, detectCfg), detectPoolConfig);//底层库算法对象池


        //初始化特征比较线程池
        GenericObjectPoolConfig comparePoolConfig = new GenericObjectPoolConfig();
        comparePoolConfig.setMaxIdle(comparePooSize);
        comparePoolConfig.setMaxTotal(comparePooSize);
        comparePoolConfig.setMinIdle(comparePooSize);
        comparePoolConfig.setLifo(false);
        EngineConfiguration compareCfg = new EngineConfiguration();
        FunctionConfiguration compareFunctionCfg = new FunctionConfiguration();
        compareFunctionCfg.setSupportFaceRecognition(true);//开启人脸识别功能
        compareCfg.setFunctionConfiguration(compareFunctionCfg);
        compareCfg.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);//图片检测模式，如果是连续帧的视频流图片，那么改成VIDEO模式
        compareCfg.setDetectFaceOrientPriority(DetectOrient.ASF_OP_0_ONLY);//人脸旋转角度
        faceEngineComparePool = new GenericObjectPool(new FaceEngineFactory(sdkLibPath, appId, sdkKey, null, compareCfg), comparePoolConfig);//底层库算法对象池
        compareExecutorService = Executors.newFixedThreadPool(comparePooSize);
    }

    @Override
    public List<FaceInfo> detectFaces(ImageInfo imageInfo) {
        FaceEngine faceEngine = null;
        try {
            faceEngine = faceEngineGeneralPool.borrowObject();
            if (faceEngine == null) {
                throw new BusinessException(ErrorCodeEnum.FAIL, "获取引擎失败");
            }
            //人脸检测得到人脸列表
            List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
            //人脸检测
            int errorCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
            if (errorCode == 0) {
                return faceInfoList;
            } else {
                log.error("人脸检测失败，errorCode：" + errorCode);
            }

        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineGeneralPool.returnObject(faceEngine);
            }
        }
        return null;
    }

    @Override
    public byte[] extractFaceFeaqture(ImageInfo imageInfo, FaceInfo faceInfo) {
        FaceEngine faceEngine = null;
        try {
            faceEngine = faceEngineGeneralPool.borrowObject();
            if (faceEngine == null) {
                throw new BusinessException(ErrorCodeEnum.FAIL, "获取引擎失败");
            }

            FaceFeature faceFeature = new FaceFeature();
            //提取人脸特征
            int errorCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfo, faceFeature);
            if (errorCode == 0) {
                return faceFeature.getFeatureData();
            } else {
                log.error("特征提取失败，errorCode：" + errorCode);
            }

        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineGeneralPool.returnObject(faceEngine);
            }
        }

        return null;
    }

    @Override
    public List<Map<String, Object>> queryFace() {
        return faceMapper.queryFace();
    }

    @Override
    public Float compareFace(ImageInfo imageInfo1, ImageInfo imageInfo2) {
        List<FaceInfo> faceInfoList1 = detectFaces(imageInfo1);
        List<FaceInfo> faceInfoList2 = detectFaces(imageInfo2);

        if (CollectionUtil.isEmpty(faceInfoList1) || CollectionUtil.isEmpty(faceInfoList2)) {
            throw new BusinessException(ErrorCodeEnum.FAIL,"未检测到人脸");
        }

        byte[] feature1 = extractFaceFeature(imageInfo1, faceInfoList1.get(0));
        byte[] feature2 = extractFaceFeature(imageInfo2, faceInfoList2.get(0));

        FaceEngine faceEngine = null;
        try {
            faceEngine = faceEngineGeneralPool.borrowObject();
            if (faceEngine == null) {
                throw new BusinessException(ErrorCodeEnum.FAIL, "获取引擎失败");
            }

            FaceFeature faceFeature1 = new FaceFeature();
            faceFeature1.setFeatureData(feature1);
            FaceFeature faceFeature2 = new FaceFeature();
            faceFeature2.setFeatureData(feature2);
            //提取人脸特征
            FaceSimilar faceSimilar = new FaceSimilar();
            int errorCode = faceEngine.compareFaceFeature(faceFeature1, faceFeature2, faceSimilar);
            if (errorCode == 0) {
                return faceSimilar.getScore();
            } else {
                log.error("特征提取失败，errorCode：" + errorCode);
            }

        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineGeneralPool.returnObject(faceEngine);
            }
        }

        return null;
    }


    /**
     * 人脸特征
     *
     * @param imageInfo
     * @return
     */
    @Override
    public byte[] extractFaceFeature(ImageInfo imageInfo, FaceInfo faceInfo) {

        FaceEngine faceEngine = null;
        try {
            faceEngine = faceEngineGeneralPool.borrowObject();
            if (faceEngine == null) {
                throw new BusinessException(ErrorCodeEnum.FAIL, "获取引擎失败");
            }

            FaceFeature faceFeature = new FaceFeature();
            //提取人脸特征
            int errorCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfo, faceFeature);
            if (errorCode == 0) {
                return faceFeature.getFeatureData();
            } else {
                log.error("特征提取失败，errorCode：" + errorCode);
            }

        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineGeneralPool.returnObject(faceEngine);
            }
        }

        return null;

    }

    @Override
    public List<Map<String, Object>> queryTicketing(String eId) {
        return faceMapper.queryTicketing(eId);
    }

    @Override
    public List<Map<String, Object>> getImageByActivityId(String aId,String  sqlDate,String  sqlDateformerly) {
        return faceMapper.getImageByActivityId(aId,sqlDate,sqlDateformerly);
    }

    @Override
    public List<Map<String, Object>> getImageByActivityIdcount(String aId,String  sqlDate,String  sqlDateformerly) {
        return faceMapper.getImageByActivityIdcount(aId,sqlDate,sqlDateformerly);
    }

    @Override
    public List<Map<String, Object>> getImageByActivityIds(String aId) {
        //先查询出图片ID，再加载图片
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        String date = sd.format(new Date());
        //date = "2023-02-16";
        List<String> ids = faceMapper.selectKeyByActivityId(aId,date,"");
        if(!ids.isEmpty()) {
            List<Map<String, Object>> resultList = faceMapper.selectImageByKeys(ids);
            return resultList;
        }
        return Collections.emptyList();
    }

    public List<Map<String, Object>> getImageByActivityIdsAbnormal(String aId) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        String date = sd.format(new Date());
        date = "2023-02-16";
        List<String> ids = faceMapper.selectKeyByActivityId(aId,date,"yes");
        if(!ids.isEmpty()) {
            List<Map<String, Object>> resultList = faceMapper.selectImageByKeys(ids);
            return resultList;
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getKeyByActivityId(String aId) {
        return faceMapper.selectKeyByActivityId(aId,"","");
    }

    @Override
    public List<Map<String, Object>> getImageByKeys(List<String> ids) {
        return faceMapper.selectImageByKeys(ids);
    }
}
