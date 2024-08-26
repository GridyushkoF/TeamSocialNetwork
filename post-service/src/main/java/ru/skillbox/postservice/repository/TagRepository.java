package ru.skillbox.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.commonlib.dto.post.wrappers.TagWrapper;
import ru.skillbox.postservice.model.entity.Tag;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findTagByName(String tagName);
    @Query("SELECT new ru.skillbox.commonlib.dto.post.wrappers.TagWrapper(t.name)" +
            " FROM Tag t WHERE t.name LIKE %:name%")
    List<TagWrapper> findTagWrappersByNameContaining(@Param("name") String name);
}
