package com.megait.soir.service;

import com.megait.soir.domain.*;
import com.megait.soir.repository.ItemRepository;
import com.megait.soir.repository.MemberRepository;
import com.megait.soir.repository.OrderItemRepository;
import com.megait.soir.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;

    /**
     * 장바구니 추가
     *
     * @param member     : 로그인한 회원
     * @param itemIdList : 장바구니에 추가할 상품 아이디
     */
    @Transactional
    public void addCart(Member member, List<Long> itemIdList) {
        member = memberRepository.getOne(member.getId());

        Orders orders = orderRepository.findByMemberAndStatus(member, Status.CART);

        if (orders == null) {
            orders = new Orders();
            orders.setStatus(Status.CART);
            orders.setMember(member);

            orderRepository.save(orders);
        }

        final Orders tmpOrders = orders;

        List<Item> itemList = itemRepository.findAllById(itemIdList);
        List<OrderItem> orderItemList = itemList.stream().map(
                item -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setItem(item);
                    orderItem.setOrder(tmpOrders);
                    orderItem.setCount(1);
                    orderItem.setOrderPrice((int) item.getPrice());

                    return orderItem;
                }
        ).collect(Collectors.toList());

        orders = orderRepository.getOne(orders.getId());

        if (orders.getOrderItems() == null) {
            orders.setOrderItems(new ArrayList<>());
        }

        orders.getOrderItems().addAll(orderItemList);
    }


    /**
     * 장바구니 목록 가져오기
     *
     * @param member : 로그인한 사용자
     * @return : 해당 사용자의 장바구니 목록
     */
    public List<OrderItem> getCart(Member member) {

        Orders cartOrder = orderRepository.findByMemberAndStatus(member, Status.CART);

        if (cartOrder == null) {
            throw new IllegalArgumentException("empty.cart");
        }
        log.info("get cart list complete.");

        return cartOrder.getOrderItems();

    }


    /**
     * 장바구니 삭제
     *
     * @param member       : 로그인한 사용자
     * @param deleteItemId : 장바구니에서 삭제할 상품 아이디
     */
    @Transactional
    public void minusCart(Member member, Long deleteItemId) {

        Orders orders = orderRepository.findByMemberAndStatus(member, Status.CART);
        List<OrderItem> orderItemList = orders.getOrderItems();

        log.info("deleteItemId : " + deleteItemId);
        orderItemList.removeIf(item -> item.getId().equals(deleteItemId));
        orderItemRepository.deleteById(deleteItemId);
        log.info("orderItemList : " + orderItemList.toString());

        orderRepository.save(orders);

    }

    /**
     * 총 가격 조회
     *
     * @param list : 장바구니 상품 리스트
     * @return : 상품 총 가격
     */
    public int getTotalPrice(List<OrderItem> list) {

        return list.stream().mapToInt(orderItem -> orderItem.getOrderPrice()).sum();
    }
}
