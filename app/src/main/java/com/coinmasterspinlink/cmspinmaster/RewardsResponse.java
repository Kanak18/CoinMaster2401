package com.coinmasterspinlink.cmspinmaster;

import java.util.List;

public class RewardsResponse {
    private String code;
    private List<Reward> msg;

    public String getCode() {
        return code;
    }

    public List<Reward> getMsg() {
        return msg;
    }

    public static class Reward {
        private String id;
        private String title;
        private String link;
        private String author_name;
        private String post_date;
        private String post_desc;
        private String author_pic;

        // Getters
        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getLink() { return link; }
        public String getAuthorName() { return author_name; }
        public String getPostDate() { return post_date; }
        public String getPostDesc() { return post_desc; }
        public String getAuthorPic() { return author_pic; }
    }
}
