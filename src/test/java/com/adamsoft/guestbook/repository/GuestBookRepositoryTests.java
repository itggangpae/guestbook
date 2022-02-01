package com.adamsoft.guestbook.repository;

import com.adamsoft.guestbook.dto.GuestBookDTO;
import com.adamsoft.guestbook.dto.PageRequestDTO;
import com.adamsoft.guestbook.dto.PageResponseDTO;
import com.adamsoft.guestbook.entity.GuestBook;
import com.adamsoft.guestbook.entity.QGuestBook;
import com.adamsoft.guestbook.service.GuestBookService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestBookRepositoryTests {
    @Autowired
    private GuestBookRepository guestBookRepository;

    //@Test
    public void insertDummies(){
        IntStream.rangeClosed(1,300).forEach(i -> {
            GuestBook guestbook = GuestBook.builder()
                    .title("Title...." + i)
                    .content("Content__ " +i)
                    .writer("user" + (i % 10))
                    .build();
            System.out.println(guestBookRepository.save(guestbook));
        });
    }

    //@Test
    public void updateTest() {
        Optional<GuestBook> result = guestBookRepository.findById(300L);
        //존재하는 번호로 테스트
        if(result.isPresent()){
            GuestBook guestBook = result.get();
            guestBook.changeTitle("Changed Title....");
            guestBook.changeContent("Changed Content....");
            guestBookRepository.save(guestBook);
        }
    }

    //@Test
    public void testQuery1() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
        QGuestBook qGuestBook = QGuestBook.guestBook; //I
        String keyword = "1";
        BooleanBuilder builder = new BooleanBuilder(); //2
        BooleanExpression expression = qGuestBook.title.contains(keyword); //3
        builder.and(expression); //4
        Page<GuestBook> result = guestBookRepository.findAll(builder, pageable); //5
        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }

    //@Test
    public void testQuery2() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
        QGuestBook qGuestBook = QGuestBook.guestBook;
        String keyword = "1";
        BooleanBuilder builder = new BooleanBuilder();
        BooleanExpression exTitle = qGuestBook.title.contains(keyword);
        BooleanExpression exContent = qGuestBook.content.contains(keyword);
        BooleanExpression exAll = exTitle.or(exContent); // 1-------------------------
        builder.and(exAll); //2-------
        builder.and(qGuestBook.gno.gt(0L)); // 3-----------------
        Page<GuestBook> result = guestBookRepository.findAll(builder, pageable);
        result.stream().forEach(guestbook -> { System.out.println(guestbook);
        });
    }

    @Autowired
    private GuestBookService gusetBookService;

    //@Test
    public void testRegister() {
        GuestBookDTO guestbookDTO = GuestBookDTO.builder()
                .title("Sample Title...")
                .content("Sample Content...")
                .writer("userO")
                .build();
        System.out.println(gusetBookService.register(guestbookDTO));
    }

    //@Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
        PageResponseDTO<GuestBookDTO, GuestBook> resultDTO = gusetBookService. getList(pageRequestDTO);
        for (GuestBookDTO guestbookDTO : resultDTO.getDtoList()) {
            System.out.println(guestbookDTO);
        }
    }

    //@Test
    public void testListInformation(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
        PageResponseDTO<GuestBookDTO, GuestBook> resultDTO = gusetBookService. getList(pageRequestDTO);

        System.out.println("PREV: "+resultDTO.isPrev());
        System.out.println("NEXT: "+resultDTO.isNext());
        System.out.println("TOTAL: " + resultDTO.getTotalPage());

        System. out. println ("------------------------------------------------------------");
        for (GuestBookDTO guestBookDTO : resultDTO.getDtoList()) {
            System.out.println(guestBookDTO);
        }
        System.out.println("========================================");

        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }


    @Test
    public void testListSearch(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).type("tc").keyword("서기호").build();
        PageResponseDTO<GuestBookDTO, GuestBook> resultDTO = gusetBookService.getList(pageRequestDTO);

        System.out.println("PREV: "+resultDTO.isPrev());
        System.out.println("NEXT: "+resultDTO.isNext());
        System.out.println("TOTAL: " + resultDTO.getTotalPage());

        System. out. println ("------------------------------------------------------------");
        for (GuestBookDTO guestBookDTO : resultDTO.getDtoList()) {
            System.out.println(guestBookDTO);
        }
        System.out.println("========================================");

        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }



}
