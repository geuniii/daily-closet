package com.megait.soir.service;


import com.megait.soir.domain.*;
import com.megait.soir.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
@Transactional // 객체가 사용하는 모든 method에 transaction이 적용된다.
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final ParentCategoryRepository parentCategoryRepository;
    private final ChildCategoryRepository childCategoryRepository;

    @PostConstruct
    public void initAlbumItems() throws IOException, ParseException {


        Resource resource = new ClassPathResource("items.json");
        JSONParser parser = new JSONParser();

        String uri = resource.getFile().toPath().toString();

        JSONArray obj = (JSONArray) parser.parse(new FileReader(uri));



        for (int i = 0; i < obj.size(); i++) {
            JSONObject object = (JSONObject) obj.get(i);
            Item item = new Item();
            item.setId((long) object.get("id"));
            item.setBrand((String) object.get("brand"));
            item.setName((String) object.get("name"));
            item.setPrice((long) object.get("price"));
            item.setCode((String) object.get("item_code"));
            item.setImg_name((String) object.get("img_name"));

            //parentcategory
            ParentCategory parent = parentCategoryRepository.findByName((String)object.get("big_category"));
            if(parent == null){
                ParentCategory newParent = new ParentCategory();
                newParent.setName((String) object.get("big_category"));
                item.setParentCategory(newParent);
            }
            else{
                item.setParentCategory(parent);
            }

            //childcategory
            ChildCategory child = childCategoryRepository.findByname((String) object.get("small_category"));
            if(child == null){
                ChildCategory newChild = new ChildCategory();
                newChild.setName((String) object.get("small_category"));
                item.setChildCategory(newChild);
            }
            else{
                item.setChildCategory(child);
            }


            // image urls
            JSONArray urlArr = (JSONArray) object.get("detail_img");


            Iterator<String> iterator1 = urlArr.iterator();
            while (iterator1.hasNext()) {
                item.getUrls().add(iterator1.next());
            }

            // sizes
            JSONArray sizeArr = (JSONArray) object.get("size");

            Iterator<String> iterator2 = sizeArr.iterator();
            while (iterator2.hasNext()) {
                item.getSizes().add(iterator2.next());
            }

            itemRepository.save(item);
        }
    }
    public List<Item> getItemList() {
        return itemRepository.findAll();
    }

    // 재우
    //Get Item by All keyword (name, brand 전체검색)
    public List<Item> findByAllKeyword(String keyword){
        return itemRepository.findByAllKeyword(keyword);
    }

        //Get Item by Name keyword (name 만 검색)
        public List<Item> findByNameKeyword(String keyword){
            return itemRepository.findByNameKeyword(keyword);
        }

        //Get Item by Brand keyword (brand 만 검색)
        public List<Item> findByBrandKeyword(String keyword){
            return itemRepository.findByBrandKeyword(keyword);
        }
        //
    /**
     * 카테고리 아이템 조회
     * @param category
     * @param pageable
     * @return
     */
    public List <Item> getItemListByCategory(String category, Pageable pageable) {
        if (category.equals("best")) {
            return itemRepository.findItem(pageable);
        } else if (category.indexOf("_") > -1) {
            return itemRepository.findItemByParentCategory(category.split("_")[0], category.split("_")[1], pageable);
        } else {
            return itemRepository.findItemByParentCategory(category, pageable);
        }
    }

    public Long getCountItemListByCategory(String category) {
        if (category.equals("best")) {
            return itemRepository.count();
        } else if (category.indexOf("_") > -1) {
            return itemRepository.countItemByParentCategory(category.split("_")[0], category.split("_")[1]);
        } else {
            return itemRepository.countItemByParentCategory(category);
        }
    }

    /**
     * 베스트 아이템 페이징처리 및 정렬 가져오기
     * @param itemRequest
     * @return
     */
    public Pageable getPageable(ItemRequest itemRequest) {
        String sort = itemRequest.getSort() != null ? itemRequest.getSort() : "liked";
        int page = itemRequest.getPage() - 1;
        int limit = itemRequest.getLimit();

        if (limit == 0) {
            limit = 20;
        }
        Sort sortBy = null;
        if (sort.equals("price_high")) {
            sortBy = Sort.by(Sort.Direction.DESC, "price");
        } else if (sort.equals("price_low")) {
            sortBy = Sort.by(Sort.Direction.ASC, "price");
        } else {
            sortBy = Sort.by(Sort.Direction.ASC, sort);
        }
        return PageRequest.of(page, limit, sortBy);
    }

    public Item findItem(Long id){

//       < 방법 1 > getOne(id) 사용
//       return itemRepository.getOne(id);

//       < 방법 2 > findById(id) 사용
//       Optional은 null 방지 클래스
        Optional<Item> optional = itemRepository.findById(id);
        return optional.orElseGet(() -> itemRepository.findById(id).get());
    }

    //추천 옷 (아우터, 상의 ,하의 )
    public Item findRecommendCategory(Long parent, Long child){
        ParentCategory parentCategory = parentCategoryRepository.getOne(parent);
        ChildCategory childCategory = childCategoryRepository.getOne(child);

        // 카테고리에 해당하는 아이템들을 받는다.
        List<Item> itemList = itemRepository.findAllByParentCategoryAndChildCategory(parentCategory, childCategory);

        // 이 중 랜덤하게 1개를 선택한다.
        int index = (int)(Math.random() * itemList.size());
        Item item = itemList.get(index);

        // 아이템의 url들 중, 0번 url을 mainUrl로
        item.setMainUrl(item.getUrls().get(0));
        return item;
    }

    // 계절에 따른 아우터 랜덤 추천 리스트
    public String random_outer_list(String season){
        Random random = new Random();

        switch (season){
            // 여름 아우터
            case "summer" :
                List<String> summer_outer = new ArrayList<>();
                summer_outer.add("27");
                summer_outer.add("17");
                summer_outer.add("15");
                String random_summer_outer = summer_outer.get(random.nextInt(summer_outer.size() - 1));
                return random_summer_outer;

            // 봄, 가을 아우터
            case "spring_fall" :
                List<String> spring_fall_outer = new ArrayList<>();
                spring_fall_outer.add("29");
                spring_fall_outer.add("27");
                spring_fall_outer.add("24");
                spring_fall_outer.add("23");
                spring_fall_outer.add("21");
                spring_fall_outer.add("20");
                spring_fall_outer.add("19");
                spring_fall_outer.add("18");
                spring_fall_outer.add("17");
                spring_fall_outer.add("15");
                spring_fall_outer.add("14");
                spring_fall_outer.add("13");
                spring_fall_outer.add("11");

                String random_spring_fall = spring_fall_outer.get(random.nextInt(spring_fall_outer.size() - 1));
                return random_spring_fall;

            // 겨울 아우터
            case "winter" :
                List<String> winter_outer = new ArrayList<>();
                winter_outer.add("28");
                winter_outer.add("26");
                winter_outer.add("24");
                winter_outer.add("22");
                String random_winter_outer = winter_outer.get(random.nextInt(winter_outer.size() - 1));
                return random_winter_outer;
        }

        return "29";
//        return null;
    }
    // 계절에 따른 상의 랜덤 추천 리스트
    public String random_top_list(String season){
        Random random = new Random();

        switch (season){

            // 여름 상의
            case "summer" :
                List<String> summer_top = new ArrayList<>();
                summer_top.add("6");
//                String random_summer_top = summer_top.get(random.nextInt(summer_top.size() - 1));
//                return random_summer_top;
                return "6";
            case  "spring_fall" :
                List<String> spring_fall_top = new ArrayList<>();
                spring_fall_top.add("2");
                spring_fall_top.add("4");
                spring_fall_top.add("5");
                spring_fall_top.add("7");
                String random_spring_fall_top = spring_fall_top.get(random.nextInt(spring_fall_top.size() - 1));
                return random_spring_fall_top;

            case "winter" :
                List<String> winter_top = new ArrayList<>();
                winter_top.add("2");
                winter_top.add("3");
                winter_top.add("5");
                String random_winter_top = winter_top.get(random.nextInt(winter_top.size() - 1));
                return random_winter_top;
        }
        return "2";
        //       return null;
    }
    // 계절에 따른 하의 랜덤 추천 리스트
    public String random_bottom_list(String season){
        Random random = new Random();
        switch (season){

            // 여름 하의
            case "summer" :
                List<String> summer_bottom = new ArrayList<>();
                summer_bottom.add("36");
                summer_bottom.add("35");
                summer_bottom.add("34");
                summer_bottom.add("30");

                String random_summer_bottom = summer_bottom.get(random.nextInt(summer_bottom.size() - 1));
                return random_summer_bottom;

            // 봄,가을 하의
            case  "spring_fall" :
                List<String> spring_fall_bottom = new ArrayList<>();
                spring_fall_bottom.add("36");
                spring_fall_bottom.add("35");
                spring_fall_bottom.add("33");
                spring_fall_bottom.add("32");
                spring_fall_bottom.add("30");

                String random_spring_fall_bottom = spring_fall_bottom.get(random.nextInt(spring_fall_bottom.size() - 1));
                return random_spring_fall_bottom;

            // 겨울 하의
            case "winter" :
                List<String> winter_bottom = new ArrayList<>();
                winter_bottom.add("33");
                winter_bottom.add("32");

                String random_winter_bottom = winter_bottom.get(random.nextInt(winter_bottom.size() - 1));
                return random_winter_bottom;
        }
        return "36";
//        return null;
    }

    public int likeCount(Item item){
        System.out.println("item_id: "+item.getId());
        int likeCount = itemRepository.countLikeMembers(item.getId());
        return  likeCount;
    }

}
