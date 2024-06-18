package ru.skillbox.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.postservice.model.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    Tag findTagByName(String tagName);
}