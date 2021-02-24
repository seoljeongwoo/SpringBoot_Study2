package com.example.project2.demo.repository;

import com.example.project2.demo.domain.Block;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BlockRepositoryTest {

    @Autowired
    private BlockRepository blockRepository;

    @Test
    void crud(){
        Block block = new Block("martin");
        block.setName("martin");
        block.setReason("친하지 않음");
        block.setStartDate(LocalDate.now());
        block.setEndDate(LocalDate.now());

        blockRepository.save(block);

        List<Block> blocks = blockRepository.findAll();
        assertEquals(blocks.size() , 3);
        assertEquals(blocks.get(0).getName(),"dennis");
        assertEquals(blocks.get(1).getName(),"sophia");
        assertEquals(blocks.get(2).getName(),"martin");
    }
}