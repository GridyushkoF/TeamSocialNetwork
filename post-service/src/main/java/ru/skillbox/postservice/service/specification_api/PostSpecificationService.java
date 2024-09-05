package ru.skillbox.postservice.service.specification_api;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.commonlib.dto.post.PostSearchDto;
import ru.skillbox.commonlib.dto.post.PostType;
import ru.skillbox.postservice.model.entity.Post;
import ru.skillbox.postservice.model.entity.Tag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PostSpecificationService {
    private static final String AUTHOR_ID = "authorId";
    public Specification<Post> getSpecificationByDto(PostSearchDto postSearchDto, Long currenAuthUserID) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.or(builder.equal(root.get("type"), PostType.POSTED)));
            addIdPredicate(postSearchDto, root, predicates);
            addAccountsPredicate(postSearchDto, root, predicates);
            addAuthorIdPredicate(postSearchDto, root, builder, predicates);
            addTitlePredicate(postSearchDto, root, builder, predicates);
            addPostTextPredicate(postSearchDto, root, builder, predicates);
            addIsDeletePredicate(postSearchDto, root, builder, predicates);
            addTagsPredicate(postSearchDto, root, predicates);
            addFromDatePredicate(postSearchDto, root, builder, predicates);
            addToDatePredicate(postSearchDto,root,builder,predicates);

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addToDatePredicate(PostSearchDto postSearchDto, Root<Post> root, CriteriaBuilder builder, List<Predicate> predicates) {
        if (postSearchDto.getDateTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("time"), postSearchDto.getDateTo()));
        }
    }

    private static void addFromDatePredicate(PostSearchDto postSearchDto, Root<Post> root, CriteriaBuilder builder, List<Predicate> predicates) {
        if (postSearchDto.getDateFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("time"), postSearchDto.getDateFrom()));
        }
    }

    private static void addTagsPredicate(PostSearchDto postSearchDto, Root<Post> root, List<Predicate> predicates) {
        if (postSearchDto.getTags() != null && !postSearchDto.getTags().isEmpty()) {
            Join<Post, Tag> tagJoin = root.join("tags", JoinType.LEFT);
            predicates.add(tagJoin.get("name").in(postSearchDto.getTags()));
        }
    }

    private static void addIsDeletePredicate(PostSearchDto postSearchDto, Root<Post> root, CriteriaBuilder builder, List<Predicate> predicates) {
        if(postSearchDto.getIsDeleted() != null) {
            predicates.add(builder.equal(root.get("isDelete"), postSearchDto.getIsDeleted()));
        }

    }

    private static void addFriendsPredicate(PostSearchDto postSearchDto) {
        if (postSearchDto.getWithFriends() != null) {
            // сделать когда будет полностью готова FriendShip-логика
        }
    }

    private static void addPostTextPredicate(PostSearchDto postSearchDto, Root<Post> root, CriteriaBuilder builder, List<Predicate> predicates) {
        if (postSearchDto.getPostText() != null && !postSearchDto.getPostText().isEmpty()) {
            predicates.add(builder.equal(root.get("postText"), postSearchDto.getPostText()));
        }
    }

    private static void addTitlePredicate(PostSearchDto postSearchDto, Root<Post> root, CriteriaBuilder builder, List<Predicate> predicates) {
        if (postSearchDto.getTitle() != null && !postSearchDto.getTitle().isEmpty()) {
            predicates.add(builder.equal(root.get("title"), postSearchDto.getTitle()));
        }
    }

    private static void addAuthorIdPredicate(PostSearchDto postSearchDto, Root<Post> root, CriteriaBuilder builder, List<Predicate> predicates) {
        if (postSearchDto.getBlockedIds() != null && !postSearchDto.getBlockedIds().isEmpty()) {
            predicates.add(builder.not(root.get(AUTHOR_ID).in(postSearchDto.getBlockedIds())));
        }
    }

    private static void addAccountsPredicate(PostSearchDto postSearchDto, Root<Post> root, List<Predicate> predicates) {
        if (postSearchDto.getAccountIds() != null && !postSearchDto.getAccountIds().isEmpty()) {
            predicates.add(root.get(AUTHOR_ID).in(postSearchDto.getAccountIds()));
        }
    }

    private static void addIdPredicate(PostSearchDto postSearchDto, Root<Post> root, List<Predicate> predicates) {
        if (postSearchDto.getIds() != null && !postSearchDto.getIds().isEmpty()) {
            predicates.add(root.get("id").in(postSearchDto.getIds()));
        }
    }
}