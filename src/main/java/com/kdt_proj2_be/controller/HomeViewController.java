package com.kdt_proj2_be.controller;

import com.kdt_proj2_be.dto.HomeViewDTO;
import com.kdt_proj2_be.service.HomeViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/homeview")
@RequiredArgsConstructor
public class HomeViewController {

    private final HomeViewService homeViewService;

    @GetMapping
    public ResponseEntity<List<HomeViewDTO>> getHomeViewData() {
        List<HomeViewDTO> homeViewList = homeViewService.getHomeViewData();
        return ResponseEntity.ok(homeViewList);
    }
}