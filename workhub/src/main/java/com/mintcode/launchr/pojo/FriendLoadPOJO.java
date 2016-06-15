package com.mintcode.launchr.pojo;

import com.mintcode.DataPOJO;
import com.mintcode.launchr.pojo.entity.FriendEntity;

import java.util.List;

/**
 * Created by JulyYu on 2016/4/5.
 */
public class FriendLoadPOJO extends DataPOJO{




    private LoadRelationLData Data;

    public LoadRelationLData getData() {
        return Data;
    }
    public void setData(LoadRelationLData data) {
        Data = data;
    }

    public class LoadRelationLData {
        private List<String> removeRelations;
        private List<FriendEntity> relations;

        public List<String> getRemoveRelations() {
            return removeRelations;
        }

        public void setRemoveRelations(List<String> removeRelations) {
            this.removeRelations = removeRelations;
        }

        public List<FriendEntity> getRelations() {
            return relations;
        }

        public void setRelations(List<FriendEntity> relations) {
            this.relations = relations;
        }
    }

}
