package com.mintcode.launchr.pojo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by JulyYu on 2016/3/28.
 */
public class RelationGroupEntity {


        private String relationGroupId;
        private String relationGroupName;
        private String defaultNameFlag;
        private List<FriendEntity> relationList;

        public String getRelationGroupId() {
            return relationGroupId;
        }
        public void setRelationGroupId(String relationGroupId) {
            this.relationGroupId = relationGroupId;
        }

        public String getRelationGroupName() {
            return relationGroupName;
        }
        public void setRelationGroupName(String relationGroupName) {
            this.relationGroupName = relationGroupName;
        }

        public String getDefaultNameFlag() {
            return defaultNameFlag;
        }
        public void setDefaultNameFlag(String defaultNameFlag) {
            this.defaultNameFlag = defaultNameFlag;
        }

        public List<FriendEntity> getRelationList() {
            return relationList;
        }
        @JsonProperty("relationList")
        public void setRelationList(List<FriendEntity> relationList) {
            this.relationList = relationList;
        }
}
