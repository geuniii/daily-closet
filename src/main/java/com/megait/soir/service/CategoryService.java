package com.megait.soir.service;

import com.megait.soir.domain.Category;
import com.megait.soir.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Slf4j
public class CategoryService {
    // Service bean이므로 IoC Container가 ComponentScan 시 객체 생성된다.

    @Autowired
    private CategoryRepository categoryRepository;

    // CategoryService객체가 생성되면 곧바로 해당 메소드를 수행한다.
    // 빈 생성 뒤 추가적인 할 일이 있는 경우 @PostConstruct를 사용하면 좋다.

}
