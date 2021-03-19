package com.megait.soir.service;


import com.megait.soir.domain.Item;
import com.megait.soir.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional // 객체가 사용하는 모든 method에 transaction이 적용된다.
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @PostConstruct
    public void initAlbumItems() throws IOException, ParseException {

        JSONParser parser = new JSONParser();
        JSONArray obj = (JSONArray) parser.parse(new FileReader("D:\\heejin\\06.spring_projects\\ClosetProject-geunii\\ClosetProject-geunii\\daily_closet_03.16\\src\\main\\resources\\items.json"));

        for (int i = 0; i < obj.size(); i++) {
            JSONObject object = (JSONObject) obj.get(i);
            Item item = new Item();
            item.setId((long) object.get("id"));
            item.setBrand((String) object.get("brand"));
            item.setName((String) object.get("name"));
            item.setPrice((long) object.get("price"));
            item.setCode((String) object.get("item_code"));

            // image urls
            JSONArray urlArr = (JSONArray) object.get("detail_img");
            ArrayList<String> urls = new ArrayList<>();
            Iterator<String> iterator = urlArr.iterator();
            while (iterator.hasNext()) {
                urls.add(iterator.next());
            }

            item.setUrlArr(urls);
            item.setImage1(urls.get(0));

            int arrlen = urls.size();

            switch (arrlen) {
                case 1:
                    item.setImage1(urls.get(0));
                    break;

                case 2:
                    item.setImage1(urls.get(0));
                    item.setImage2(urls.get(1));
                    break;

                case 3:
                    item.setImage1(urls.get(0));
                    item.setImage2(urls.get(1));
                    item.setImage3(urls.get(2));
                    break;

                case 4:
                    item.setImage1(urls.get(0));
                    item.setImage2(urls.get(1));
                    item.setImage3(urls.get(2));
                    item.setImage4(urls.get(3));
                    break;

                default:
                    item.setImage1(urls.get(0));
                    item.setImage2(urls.get(1));
                    item.setImage3(urls.get(2));
                    item.setImage4(urls.get(3));
                    item.setImage5(urls.get(4));
                    break;


            }


//
//            ArrayList<String> urls = (ArrayList<String>) object.get("detail_img");
//            System.out.println("url:::::::::::"+urls);
//
//            // category
//            ArrayList<Category> categorys = new ArrayList<>();
//            Category parent = new Category();
//            Category child = new Category();
//            parent.setName((String) object.get("big_category"));
//            child.setName((String) object.get("small_category"));
//            child.setParent(parent);
//            categorys.add(parent);
//            categorys.add(child);
//            item.setCategories(categorys);
//
//            // image_urls
//            ArrayList<Image> images = new ArrayList<>();
//            List<String> imageList = (List<String>) object.get("detail_img");
//            for (int j = 0; j < imageList.size(); j++) {
//                Image image = new Image();
//                image.setUrl(imageList.get(j));
//                images.add(image);
//            }
//            item.setImageUrl(images);
//

            itemRepository.save(item);
        }
    }

//    @PostConstruct
//    public void initBookItems() throws IOException {
////        Resource resource = new ClassPathResource("album.CSV");
////        List<String> list = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8);
////
////        Stream<String> stream = list.stream();
////
////        Stream<Item> stream2 = stream.map(
////                line->{
////                    String[] split = line.split("\\|");
////                    Book book = new Book();
////                    book.setName(split[0]);
////                    book.setImageUrl(split[1]);
////                    book.setPrice(Integer.parseInt(split[2]));
////                    return book;
////                });
////
////        List<Item> items = stream2.collect(Collectors.toList());
////
////        // 위에서 만든 List<에 저장
////        itemRepository.saveAll(items);
//
//    }

    public List<Item> getItemList() {
        return itemRepository.findAll();
    }

    public Item findItem(Long id) {

        Optional<Item> optional = itemRepository.findById(id);
//        if(optional.isPresent()){
//            return optional.get(); // findById()로 받으면 casting error -> get() 추가
//        }
        // 위 if문을 아래와 같이 바꿔도 된다
        return optional.orElse(null);


    }
}
