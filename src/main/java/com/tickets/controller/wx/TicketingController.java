package com.tickets.controller.wx;

import com.tickets.annotations.Authentication;
import com.tickets.dto.ResponseResult;
import com.tickets.dto.TicketingSaveDto;
import com.tickets.service.TicketingStaffService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "微信票务接口")
@RestController
@RequestMapping("/wechat/ts")
public class TicketingController {

    @Autowired
    private TicketingStaffService ticketingStaffServicef;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @PostMapping("/add")
    public ResponseResult add(@RequestBody TicketingSaveDto ticketingSaveDto) {
        ticketingStaffServicef.save(ticketingSaveDto);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @GetMapping("/byWid")
    public ResponseResult getById() {
        String id = (String) httpServletRequest.getAttribute("id");
        List<Map<String, Object>> byWid = null;
        if (!StringUtils.isEmpty(id)) {
            byWid = ticketingStaffServicef.getById(id);
        }
        return ResponseResult.SUCCESS(byWid);
    }
}
