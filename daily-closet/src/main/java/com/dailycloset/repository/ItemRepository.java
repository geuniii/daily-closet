package com.dailycloset.repository;

import com.dailycloset.domain.Item;
import com.dailycloset.domain.ParentCategory;
import com.dailycloset.domain.ChildCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    /**
     * 해당 페이지의 상품
     * @param pageable : 조회 페이지
     * @return : 해당 페이지의 상품
     */
    @Query("SELECT i FROM Item i")
    List<Item> findItem(Pageable pageable);

    /**
     * 상위 카테고리별 상품 조회
     * @param category : 상위 카테고리
     * @param pageable : 조회 페이지
     * @return 해당 페이지, 카테고리의 상품
     */
    @Query("SELECT i FROM Item i WHERE i.parentCategory IN (SELECT pc.id FROM ParentCategory pc WHERE pc.name = ?1)")
    List<Item> findItemByParentCategory(String category, Pageable pageable);

    /**
     * 악세서리 조회
     * @param category_1 : 상위 카테고리
     * @param category_2 : 하위 카테고리
     * @param pageable : 조회 페이지
     * @return : 해당 페이지, 카테고리의 악세서리 상품
     */
    @Query("SELECT i FROM Item i WHERE i.parentCategory IN (SELECT pc.id FROM ParentCategory pc WHERE pc.name = ?1 or pc.name = ?2)")
    List<Item> findItemByParentCategory(String category_1, String category_2, Pageable pageable);

    /**
     * 카테고리별 상품 갯수 조회
     * @param category : 조회 카테고리
     * @return : 해당 카테고리 상품의 갯수
     */
    @Query(value = "SELECT count(i) FROM Item i WHERE i.parentCategory IN (SELECT pc.id FROM ParentCategory pc WHERE pc.name = ?1)")
    Long countItemByParentCategory(String category);

    /**
     * 악세서리 상품 갯수 조회
     * @param category_1: 상위 카테고리
     * @param category_2: 하위 카테고리
     * @return : 악세서리 상품의 갯수
     */
    @Query(value = "SELECT count(i) FROM Item i WHERE i.parentCategory IN (SELECT pc.id FROM ParentCategory pc WHERE pc.name = ?1 or pc.name = ?2)")
    Long countItemByParentCategory(String category_1, String category_2);


    /**
     * 상품 검색
     * @param keyword : 검색어
     * @return : 검색어에 해당하는 상품
     */
    @Query(value = "select * from  Item e where e.Name like %:keyword% or e.Brand like %:keyword%", nativeQuery = true)
    List<Item> findByAllKeyword(@Param("keyword") String keyword);

    /**
     * 상품명별 검색
     * @param keyword : 검색어(상품명)
     * @return : 상품명에 해당하는 상품
     */
    @Query(value = "select * from  Item e where e.Name like %:keyword%", nativeQuery = true)
    List<Item> findByNameKeyword(@Param("keyword") String keyword);

    /**
     * 브랜드별 검색
     * @param keyword : 검색어(브랜드명)
     * @return : 브랜드명에 해당하는 상품
     */
    @Query(value = "select * from  Item e where e.Brand like %:keyword%", nativeQuery = true)
    List<Item> findByBrandKeyword(@Param("keyword") String keyword);

    /**
     * 카테고리별 검색
     * @param parent : 상위 카테고리
     * @param child : 하위 카테고리
     * @return : 카테고리에 해당하는 상품
     */
    List<Item> findAllByParentCategoryAndChildCategory(ParentCategory parent, ChildCategory child);

    /**
     * 찜 갯수 조회
     * @param id : 상품 아이디
     * @return : 해당 상품의 찜 갯수
     */
    @Query(value = "SELECT COUNT(*) FROM MEMBER_LIKES m WHERE m.LIKES_ID =:id", nativeQuery = true)
    int countLikeMembers(@Param("id") Long id);
}









