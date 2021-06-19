package com.megait.soir.controller;

import com.google.gson.JsonObject;
import com.megait.soir.form.CodyForm;
import com.megait.soir.domain.*;
import com.megait.soir.form.ReviewForm;
import com.megait.soir.form.SearchForm;
import com.megait.soir.repository.MemberRepository;
import com.megait.soir.service.*;
import com.megait.soir.user.CurrentUser;
import com.megait.soir.user.SignUpForm;
import com.megait.soir.user.SignUpValidator;
import com.megait.soir.user.UpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final SignUpValidator signUpValidator;
    private final MemberService memberService;
    private final ItemService itemService;
    private final MemberRepository memberRepository;
    private final OrderService orderService;
    private final SendEmailService sendEmailService;
    private final CodyService codyService;
    private final ReviewService reviewService;
    private final WeatherService weatherService;
    private final CityNameService cityNameService;
    private final DateService dateService;

    /**
     * 메인 페이지
     * @param member : 로그인된 사용자
     * @param keyword : 상품 검색어
     * @param searchType : 전체 검색 / 브랜드 검색
     * @param city : 현재 위치
     * @return : 메인 페이지
     */
    @GetMapping("/")
    public String index(@CurrentUser Member member, Model model, String keyword, String searchType, String city){

        model.addAttribute(new SearchForm());

        List<String> codyIdList = codyService.codyLikeRank();

        List<Cody> codyList = new ArrayList<>();
        if(codyIdList !=null){
            for(int i = 0; i<codyIdList.size(); i++){
                Cody cody = codyService.getOne(Long.parseLong(String.valueOf(codyIdList.get(i))));
                codyList.add(cody);
                System.out.println(codyList.get(i).getId());
            }
        }
        model.addAttribute("codyList",codyList);

        model.addAttribute("itemList",itemService.getItemList());

        String baseDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String meridien = dateService.currentHour();
        if(city != null) {
            String cityName = cityNameService.renameCity(city);
            model.addAttribute("currentTemperature", weatherService.findCurrentDateTemperature(baseDate, cityName, meridien));
            model.addAttribute("weatherList", weatherService.findCurrentLocalWeather(cityName));
            System.out.println("주소가 null 아님");

        }
        else{
            model.addAttribute("currentTemperature", weatherService.findCurrentDateTemperature(baseDate, "서울_인천_경기도", meridien));
            System.out.println("주소가 null일 경우");
        }


        Long parent1 = Long.valueOf("12"); // 아우터값 (고정값)
        Long child1 = Long.valueOf(itemService.random_outer_list("summer"));

        Long parent2 = Long.valueOf("3"); // 상의값 (고정값)
        Long child2 = Long.valueOf(itemService.random_top_list("summer"));

        Long parent3 = Long.valueOf("31"); // 하의값 (고정값)
        Long child3 = Long.valueOf(itemService.random_bottom_list("summer"));

        model.addAttribute("outer", itemService.findRecommendCategory(parent1, child1));

        model.addAttribute("top", itemService.findRecommendCategory(parent2, child2));

        model.addAttribute("bottom", itemService.findRecommendCategory(parent3, child3));
    return "/view/index";
    }

    /**
     * 도시 날씨 조회
     * @param city : 현재 위치
     * @return : 날씨 페이지
     */
    @GetMapping("/weather/weatherList")
    public String weatherCityList(String city, Model model) {
        model.addAttribute("weatherList", weatherService.findByWeatherCity(city));
        return "/view/weather :: #weatherList";
    }

    /**
     * 날씨별 옷 추천
     * @return : 상품 추천 fragment
     */
    @GetMapping("/daily-recommend")
    public String daily_recommend(Model model, String city){

        Calendar calendar = Calendar.getInstance(); // 현재 '시간' 만 얻기
        SimpleDateFormat sdf = new SimpleDateFormat("HH"); // 24시간제로 표시
        String hour = sdf.format(calendar.getTime());
        int currentHour = Integer.parseInt(hour);

        String baseDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String meridien = dateService.currentHour();
        if(city != null) {
            String cityName = cityNameService.renameCity(city);
            model.addAttribute("currentTemperature", weatherService.findCurrentDateTemperature(baseDate, cityName, meridien));
            model.addAttribute("weatherList", weatherService.findCurrentLocalWeather(cityName));
            System.out.println("주소가 null 아님");

        }
        else{
            model.addAttribute("currentTemperature", weatherService.findCurrentDateTemperature(baseDate, "서울_인천_경기도", meridien));
            System.out.println("주소가 null일 경우");
        }

        String season = "winter";
        if(currentHour > 11 && currentHour < 24 ){ //오후
            Long pm_temperature = weatherService.pm_temperature();
            // 봄, 가을 날씨 온도
            if (pm_temperature > Long.valueOf("12") && pm_temperature < Long.valueOf("28")){
                season = "spring_fall";
            }
            else if (pm_temperature <= Long.valueOf("12")){ // 겨울
                season = "winter";
            }
            else if(pm_temperature >= Long.valueOf("28")){// 여름
                season = "summer";
            }
        }
        else{ //오전
            Long am_temperature = weatherService.am_temperature();
            // 봄, 가을 날씨 온도
            if (am_temperature > Long.valueOf("12") && am_temperature < Long.valueOf("28")){
                season = "spring_fall";
            }
            else if (am_temperature <= Long.valueOf("12")){ // 겨울
                season = "winter";
            }
            else if(am_temperature >= Long.valueOf("28")){// 여름
                season = "summer";
            }
        }

        Long parent1 = Long.valueOf("12");
        Long child1 = Long.valueOf(itemService.random_outer_list(season));

        Long parent2 = Long.valueOf("3");
        Long child2 = Long.valueOf(itemService.random_top_list(season));

        Long parent3 = Long.valueOf("31");
        Long child3 = Long.valueOf(itemService.random_bottom_list(season));


        model.addAttribute("outer", itemService.findRecommendCategory(parent1, child1));

        model.addAttribute("top", itemService.findRecommendCategory(parent2, child2));

        model.addAttribute("bottom", itemService.findRecommendCategory(parent3, child3));

        return "/view/daily-recommend";
    }


    /**
     * 회원 정보
     * @param member : 로그인한 사용자
     * @return : 회원 정보 페이지
     */
    @GetMapping("/memberInfo")
    public String findMember(@CurrentUser Member member, Model model) {
        model.addAttribute(member);
        return "/view/memberInfo";
    }

    /**
     * 회원 정보 수정 페이지
     * @param member : 로그인한 사용자
     * @return : 회원 정보 수정 페이지
     */
    @GetMapping("/update-memberInfo")
    public String update(@CurrentUser Member member, Model model) {
        model.addAttribute(member);
        model.addAttribute(new UpdateForm());
        return "/view/update-memberInfo";
    }

    /**
     * 회원 정보 수정 내용 저장
     * @param member : 로그인한 사용자
     * @param updateForm : 수정 내용
     * @return : 메인 페이지
     */
    @PostMapping("/update-memberInfo") // post 요청 시 실행되는 메소드 -> 즉 회원가입 form 작성 시 수행된다.
    public String updateSubmit(@CurrentUser Member member, @Valid UpdateForm updateForm) {

        memberService.updateMember(member, updateForm);
        return "redirect:/";
    }



    /**
     * 회원 탈퇴
     * @param id : 로그인한 사용자 ID
     * @return : 요청 결과
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public Map<String, Object> delete(@PathVariable Long id) {
        memberService.delete(id);

        Map<String, Object> json = new HashMap<>();
        json.put("status", 200);
        json.put("id", id);
        return json;
    }

    /**
     * 카테고리 별 아이템 조회
     * @param itemRequest : 카테고리
     * @return : 아이템 페이지
     */
    @GetMapping("/itemList")
    public String itemList(ItemRequest itemRequest, Model model) {
        String categoryName = itemRequest.getCategoryName();
        Pageable pageable = itemService.getPageable(itemRequest);

        Paginator paginator = new Paginator(5, itemRequest.getLimit(), itemService.getCountItemListByCategory(categoryName));
        Map<String, Object> pageInfo = paginator.getFixedBlock(itemRequest.getPage());

        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("itemList", itemService.getItemListByCategory(categoryName, pageable));
        model.addAttribute("categoryName", categoryName);

        return "/view/category";
    }


    /**
     * 상품 검색
     * @param searchForm : 검색어, 검색 범위
     * @return 상품 페이지
     */
    @GetMapping("/searchList")
    public String searchList(@Valid SearchForm searchForm, Model model) {


        System.out.println("durlsfdsfse");

        if(searchForm.getOption().equals("item_all")){
            model.addAttribute("itemList", itemService.findByAllKeyword(searchForm.getKeyword()));
        }
        else if(searchForm.getOption().equals("item_name")){
            model.addAttribute("itemList", itemService.findByNameKeyword(searchForm.getKeyword()));
        }
        else if(searchForm.getOption().equals("brand_name")){
            model.addAttribute("itemList", itemService.findByBrandKeyword(searchForm.getKeyword()));
        }

        else{
            model.addAttribute("itemList", itemService.getItemList());
        }
        return "/view/category";
    }


    /**
     * 회원 가입 페이지
     * @return : 회원가입 페이지
     */
    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute(new SignUpForm()); // 빈 DTO를 view에게 전달?
        return "/view/signup";
    }

    /**
     * 회원 가입 저장
     * @param signUpForm : 가입 회원 정보
     * @param errors : 이메일 유효성 검사
     * @return : 메인 페이지
     */
    @PostMapping("/signup") // post 요청 시 실행되는 메소드 -> 즉 회원가입 form 작성 시 수행된다.
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {


        signUpValidator.validate(signUpForm, errors); // 여기서 이메일 유효성 검증
        log.info("check validation complete!");

        Member member = memberService.createNewMember(signUpForm);
        memberService.sendSignUpEmail(member);
        memberService.autologin(member); // 해당 member를 자동 로그인 하기

        return "redirect:/"; // root로 redirect
    }


    /**
     * 이메일 체크
     * @param email : 가입 이메일
     * @param token : 이메일 인증 토큰
     * @return : 이메일 인증 결과 페이지
     */
    @GetMapping("/check-email-token")
    public String checkEmailToken(String email, String token, Model model) {
        EmailCheckStatus status = memberService.processSignUp(email, token);

        switch (status) {
            case WRONG_EMAIL:
                model.addAttribute("error", "wrong email.");
                break;

            case WRONG_TOKEN:
                model.addAttribute("error", "wrong token.");
                break;

            case COMPLETE:
                break;

            case MODIFIED:
                model.addAttribute("error", "already verified account.");
                break;
        }

        model.addAttribute("email", email);
        return "/view/check-email-result";
    }


    /**
     * 로그인
     * @return : 로그인 페이지
     */
    @GetMapping("/login")
    public String login() {

        return "/view/login";
    }


    /**
     * 패스워드 변경 (비밀번호 분실)
     * @return 패스워드 변경 페이지
     */
    @GetMapping("/send-reset-password-link")
    public String sendResetPasswordView() {

        return "/view/send-reset-password-link";
    }


    /**
     * 패스워드 변경을 위한 메일 전송
     * (해당 email로 '/reset-password?email=이메일&token=토큰' 형태의 문장을 email로 전송)
     * @param email : 로그인 이메일
     * @return : 결과 알림 페이지
     */
    @PostMapping("/send-reset-password-link")
    public String sendResetPassword(String email, Model model) {
        // 해당 email로 '/reset-password?email=이메일&token=토큰' 형태의 문장을 email로 전송

        try {
            memberService.sendResetPasswordEmail(email);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error_code", "password.reset.failed");
            return "/view/notify";
        }

        model.addAttribute("email", email);
        model.addAttribute("result_code", "password.reset.send");

        return "/view/notify";
    }


    /**
     * 패스워드 수정
     * (email과 token 무결성 체크)
     * @param email :로그인된 이메일
     * @param token :인증용 토큰
     * @return :(로그인 된) 패스워드 수정 페이지
     */
    @GetMapping("/reset-password")
    public String resetPasswordView(String email, String token, Model model) {

        boolean result = memberService.checkEmailToken(email, token);

        model.addAttribute("result", result);
        model.addAttribute("email", email);

        return "/view/reset-password";

    }

    /**
     * 패스워드 변경 실행
     * @param email :로그인된 이메일
     * @param password : 새 패스워드
     * @return : 결과 알림 페이지
     */
    @PostMapping("/reset-password")
    public String resetPassword(String email, String password, Model model) {

        memberService.resetPassword(email, password);
        model.addAttribute("result_code", "password.reset.complete");

        return "/view/notify";
    }


    /**
     * 상품 상세 페이지
     * @param member : 로그인한 사용자
     * @param id : 상품 아이디
     * @return : 상품 상세 페이지
     */
    @GetMapping("/store/detail/{id}")
    public String detail(@CurrentUser Member member, @PathVariable Long id, Model model) {
        log.info("id : " + id);

        Item item = itemService.findItem(id);

        model.addAttribute(new ReviewForm());
        model.addAttribute("like_status", false);
        if (member != null) {
            member = memberRepository.findByEmail(member.getEmail());
            model.addAttribute("like_status", member.getLikes().contains(item));
        }
        model.addAttribute("item", item);
        model.addAttribute("currentUser",member);
        model.addAttribute("likeMember",itemService.likeCount(item));

        System.out.println();

        return "/view/detail";
    }

    /**
     * 상품 찜하기
     * @param member : 로그인한 사용자
     * @param itemId : 상품 아이디
     * @return : 찜 / 찜 취소 결과
     */
    @GetMapping("/store/like")
    @ResponseBody
    public String addLike(@CurrentUser Member member, @RequestParam("id") Long itemId) {

        boolean result = false;

        JsonObject jsonObject = new JsonObject();

        try {
            result = memberService.addLike(member, itemId);

            if (result) {
                jsonObject.addProperty("message", "Add like list Complelte!"); // 찜 목록 추가
            }

            else {
                jsonObject.addProperty("message", "Delete from like list."); // 찜 목록 삭제
            }
            jsonObject.addProperty("status", result);
        } catch (IllegalArgumentException e) {
            jsonObject.addProperty("message", "Wrong access.");
        } catch (UsernameNotFoundException e) {

        }
        return jsonObject.toString();
    }


    /**
     * 찜 목록
     * @param member : 로그인한 사용자
     * @return : 찜 목록 페이지
     */
    @GetMapping("/store/like-list")
    public String likeList(@CurrentUser Member member, Model model) {
        List<Item> likeList = memberService.getLikeList(member);
        model.addAttribute("likeList", likeList);
        return "/view/like-list";
    }


    /**
     * 장바구니 추가
     * @param itemId : 상품 아이디
     * @param member: 로그인한 사용자
     * @return : 장바구니 목록 조회 함수
     */
    @PostMapping("/cart/list")
    public String addCart(@RequestParam("item_id") String[] itemId, @CurrentUser Member member, Model model) {

        Long[] idArr = Arrays.stream(itemId).map(Long::parseLong).toArray(Long[]::new);
        List<Long> itemIdList = List.of(idArr);

        orderService.addCart(member, itemIdList);

        return cartList(member, model);
    }


    /**
     * 장바구니 목록
     * @param member : 로그인한 사용자
     * @return : 장바구니 목록 페이지
     */
    @GetMapping("/cart/list")
    public String cartList(@CurrentUser Member member, Model model) {

        try {
            List<OrderItem> cartList = orderService.getCart(member);
            model.addAttribute("cartList", cartList);
            model.addAttribute("totalPrice", orderService.getTotalPrice(cartList));

        } catch (IllegalArgumentException e) {
            model.addAttribute("error_message", e.getMessage());
        }

        return "/view/cart";
    }

    /**
     * 장바구니 취소
     * @param itemId : 상품 아이디
     * @param member : 로그인한 사용자
     * @return : 장바구니 목록 함수
     */
    @GetMapping("/cart/minus")
    public String cartMinus(@RequestParam("id") String itemId, @CurrentUser Member member, Model model){

        Long deleteItemId = Long.parseLong(itemId);
        orderService.minusCart(member, deleteItemId);

        return cartList(member, model);
    }

    /**
     * 임시 패스워드 제공
     * @param email : 로그인 할 이메일
     * @return : 임시 비밀번호, 실행 결과
     * @throws Exception
     */
    @PostMapping("/find-pw")
    @ResponseBody
    public String find_pw_form(String email) throws Exception {

        String uuid = null;
        for (int i = 0; i < 5; i++) {
            uuid = UUID.randomUUID().toString().replaceAll("-", ""); // -를 제거해 주었다.
            uuid = uuid.substring(0, 10); //uuid를 앞에서부터 10자리 잘라줌.
            System.out.println(i + ") " + uuid);
        }


        sendEmailService.sendEmail(email, "회원님의 임시비밀번호는 : " + uuid + " 입니다.", "Daily Closet 에서 임시 비밀번호 발급 ");
        memberService.resetPassword(email, uuid);

        JsonObject json = new JsonObject();
        json.addProperty("status", 200);
        return json.toString();

    }

    /**
     * 상품 코디
     * @param member : 로그인한 사용자
     * @return : 상품 코디 페이지
     */
    @GetMapping("/cody")
    public String cody(@CurrentUser Member member, Model model) {

        model.addAttribute(new CodyForm());

        List<Item> likeList = memberService.getLikeList(member);
        List<Item> top = new ArrayList<>();
        List<Item> outer = new ArrayList<>();
        List<Item> bottom = new ArrayList<>();
        List<Item> acc = new ArrayList<>();
        List<Item> shoes = new ArrayList<>();

        for(int i = 0; i<likeList.size(); i++){ // 찜한 상품들 조회
            if(likeList.get(i).getParentCategory().getName().equals("상의")){
                top.add(likeList.get(i));
            }
            if(likeList.get(i).getParentCategory().getName().equals("아우터")){
                outer.add(likeList.get(i));
            }
            if(likeList.get(i).getParentCategory().getName().equals("바지")){
                bottom.add(likeList.get(i));
            }
            if(likeList.get(i).getParentCategory().getName().equals("신발")||likeList.get(i).getParentCategory().getName().equals("스니커즈") ){
                shoes.add(likeList.get(i));
            }
            if(likeList.get(i).getParentCategory().getName().equals("가방")){
                acc.add(likeList.get(i));
            }
        }
        model.addAttribute("topList", top);
        model.addAttribute("outerList", outer);
        model.addAttribute("bottomList", bottom);
        model.addAttribute("accList", acc);
        model.addAttribute("shoesList", shoes);
        model.addAttribute("member", member);

        return "/view/cody";
    }

    /**
     * 상품 코디 저장
     * @param member : 로그인한 사용자
     * @param codyForm : 선택한 상품, 사이즈
     * @return : 상품 코디 페이지
     */
    @PostMapping("/cody")
    public String codySubmit(@CurrentUser Member member, @Valid CodyForm codyForm) {

        Cody cody = codyService.createNewCody(member,codyForm);

        return "redirect:/cody"; // root로 redirect
    }

    /**
     * 후기 작성, 수정
     * @param member : 로그인한 사용자
     * @param reviewForm : 후기 제목, 내용
     * @return : 상품 상세 페이지
     * @throws Exception
     */
    @PostMapping("/review")
    public String create(@CurrentUser Member member, @Valid ReviewForm reviewForm) throws Exception {
        log.info("POST /review : " + reviewForm.toString());
        Item item = itemService.findItem(reviewForm.getItemId());

        if(reviewForm.getReviewId()!=null){// 리뷰 수정
            Optional<Review> optional = reviewService.findById(reviewForm.getReviewId());
            if(optional!=null){
                Review review = optional.get();
                reviewService.updateReview(review,reviewForm);
                return "redirect:/store/detail/"+reviewForm.getItemId();
            }
        }

        reviewService.createReview(member,item,reviewForm);// 리뷰 생성
        return "redirect:/store/detail/"+reviewForm.getItemId();
    }

    /**
     * 후기 삭제
     * @param reviewId : 후기 아이디
     * @param itemId : 상품 아이디
     * @return
     */
    @PostMapping("/review/delete")
    public String deleteReview(@RequestParam("reviewId") Long reviewId, @RequestParam("itemId") Long itemId ){
        log.info("DELETE no : " + reviewId);
        reviewService.deleteReview(reviewId);
        return "redirect:/store/detail/"+itemId;

    }

    /**
     * 사용자 코디 리스트
     * @param member : 로그인한 사용자
     * @return : 사용자 코디 리스트 페이지
     */
    @GetMapping("/codyList")
    public String codyList(@CurrentUser Member member, Model model) {

        model.addAttribute("codyList",codyService.getCodyList(member));
        return "/view/codyList";
    }

    /**
     * 모든 코디 리스트
     * @param member : 로그인한 사용자
     * @return : 모든 코디 리스트 페이지
     */
    @GetMapping("/allCodyList")
    public String allCodyList(@CurrentUser Member member,Model model) {

        model.addAttribute("codyList",codyService.getAllList());

        HashMap hashMap = new HashMap();

        if (member != null) {
            member = memberRepository.findByEmail(member.getEmail());
            for(Cody cody:codyService.getAllList()){
                hashMap.put(cody.getId(),member.getCodyLikes().contains(cody));
            }
            System.out.println(hashMap);
        }
        System.out.println();
        model.addAttribute("codyLikes",hashMap);
        return "/view/allCodyList";
    }

    /**
     * 코디 찜하기 / 찜하기 취소
     * @param member : 로그인한 사용자
     * @param codyId : 코디 아이디
     * @return : 찜 / 찜 취소 결과
     */
    @GetMapping("/cody/like")
    @ResponseBody
    public String addCodyLike(@CurrentUser Member member, @RequestParam("id") Long codyId) {

        boolean result = false;

        JsonObject jsonObject = new JsonObject();

        try {
            System.out.println("코디라이크 들어옴");
            result = memberService.addCodyLike(member, codyId);

            if (result) {// 찜 목록 추가
                jsonObject.addProperty("message", "Add like list Complelte!");
            }

            else {// 찜 목록 삭제
                jsonObject.addProperty("message", "Delete from like list.");
            }
            jsonObject.addProperty("status", result);
        } catch (IllegalArgumentException e) {
            jsonObject.addProperty("message", "Wrong access.");
        } catch (UsernameNotFoundException e) {

        }
        return jsonObject.toString();
    }
}
