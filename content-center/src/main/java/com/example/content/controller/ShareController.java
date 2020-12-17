package com.example.content.controller;

import com.example.annotation.CheckLogin;
import com.example.common.domain.dto.content.ShareDTO;
import com.example.content.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @date 2020/12/03
 * @time 20:35
 */
@RestController
@RequestMapping("/share")
public class ShareController {

    @Autowired
    ShareService shareService;

    @GetMapping("/{id}")
    @CheckLogin
    public ShareDTO findById(@PathVariable Integer id) {
        return shareService.findById(id);
    }
}
