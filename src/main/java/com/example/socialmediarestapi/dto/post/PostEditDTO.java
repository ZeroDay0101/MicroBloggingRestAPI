package com.example.socialmediarestapi.dto.post;

public class PostEditDTO {
    private final long postId;
    private final String editedPostText;

    public PostEditDTO(long postId, String editedPostText) {
        this.postId = postId;
        this.editedPostText = editedPostText;
    }

    public long getPostId() {
        return postId;
    }

    public String getEditedPostText() {
        return editedPostText;
    }
}
