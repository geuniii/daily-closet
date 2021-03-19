package com.megait.soir.service;

import com.megait.soir.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class CategoryService {
    // Service bean이므로 IoC Container가 ComponentScan 시 객체 생성된다.

    @Autowired
    private CategoryRepository categoryRepository;

}

