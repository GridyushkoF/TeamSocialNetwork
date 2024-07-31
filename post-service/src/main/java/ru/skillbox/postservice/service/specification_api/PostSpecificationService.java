package ru.skillbox.postservice.service.specification_api;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.commondto.post.PostSearchDto;
import ru.skillbox.commondto.post.PostType;
import ru.skillbox.postservice.model.entity.Post;
import ru.skillbox.postservice.model.entity.Tag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PostSpecificationService {
    public Specification<Post> getSpecificationByDto(PostSearchDto postSearchDto, Long currenAuthUserID) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.or(builder.equal(root.get("type"), PostType.POSTED),
                    builder.equal(root.get("authorId"), currenAuthUserID)));
            addIdPredicate(postSearchDto, root, predicates);
            addAccountsPredicate(postSearchDto, root, predicates);
            addAuthorIdPredicate(postSearchDto, root, builder, predicates);
            addAuthorPredicate(postSearchDto, root, builder, predicates);
            addTitlePredicate(postSearchDto, root, builder, predicates);
            addPostTextPredicate(postSearchDto, root, builder, predicates);
            addFriendsPredicate(postSearchDto);
            addIsDeletePredicate(postSearchDto, root, builder, predicates);
            addTagsPredicate(postSearchDto, root, predicates);
            addFromDatePredicate(postSearchDto, root, builder, predicates);
            addToDatePredicate(postSearchDto, root, builder, predicates);
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addToDatePredicate(PostSearchDto postSearchDto, Root<Post> root, CriteriaBuilder builder, List<Predicate> predicates) {
        if (postSearchDto.getDateTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("publishDate"), new Date(postSearchDto.getDateTo())));
        }
    }

    private static void addFromDatePredicate(PostSearchDto postSearchDto, Root<Post> root, CriteriaBuilder builder, List<Predicate> predicates) {
        if (postSearchDto.getDateFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("publishDate"), new Date(postSearchDto.getDateFrom())));
        }
    }

    private static void addTagsPredicate(PostSearchDto postSearchDto, Root<Post> root, List<Predicate> predicates) {
        if (postSearchDto.getTags() != null && !postSearchDto.getTags().isEmpty()) {
            Join<Post, Tag> tagJoin = root.join("tags", JoinType.LEFT);
            predicates.add(tagJoin.get("name").in(postSearchDto.getTags()));
        }
    }

    private static void addIsDeletePredicate(PostSearchDto postSearchDto, Root<Post> root, CriteriaBuilder builder, List<Predicate> predicates) {
        predicates.add(builder.equal(root.get("isDelete"), postSearchDto.isDelete()));
    }

    private static void addFriendsPredicate(PostSearchDto postSearchDto) {
        if (postSearchDto.isWithFriends()) {
            //TODO: сделать когда будет полностью готова FriendShip-логика
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

    private static void addAuthorPredicate(PostSearchDto postSearchDto, Root<Post> root, CriteriaBuilder builder, List<Predicate> predicates) {
        // if (!postSearchDto.getAuthor().isEmpty()) {
        //     predicates.add(builder.equal(root.get("author"), postSearchDto.getAuthor()));
        // }
    }

    private static void addAuthorIdPredicate(PostSearchDto postSearchDto, Root<Post> root, CriteriaBuilder builder, List<Predicate> predicates) {
        if (postSearchDto.getBlockedIds() != null && !postSearchDto.getBlockedIds().isEmpty()) {
            predicates.add(builder.not(root.get("authorId").in(postSearchDto.getBlockedIds())));
        }
    }

    private static void addAccountsPredicate(PostSearchDto postSearchDto, Root<Post> root, List<Predicate> predicates) {
        if (postSearchDto.getAccountIds() != null && !postSearchDto.getAccountIds().isEmpty()) {
            predicates.add(root.get("authorId").in(postSearchDto.getAccountIds()));
        }
    }

    private static void addIdPredicate(PostSearchDto postSearchDto, Root<Post> root, List<Predicate> predicates) {
        if (postSearchDto.getIds() != null && !postSearchDto.getIds().isEmpty()) {
            predicates.add(root.get("id").in(postSearchDto.getIds()));
        }
    }
}