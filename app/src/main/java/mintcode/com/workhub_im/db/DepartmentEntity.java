package mintcode.com.workhub_im.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "DEPARTMENT_ENTITY".
 */
public class DepartmentEntity {

    private Long id;
    private String showId;
    private String cShowId;
    private String dName;
    private String dLevel;
    private String dParentName;
    private String dParentShowId;
    private String cName;
    private String createUserName;
    private Integer dSort;
    private Integer dAvailableCount;
    private Integer dUnAvailableCount;
    private Integer childDeptCount;
    private String extendField;
    private String otherField;

    public DepartmentEntity() {
    }

    public DepartmentEntity(Long id) {
        this.id = id;
    }

    public DepartmentEntity(Long id, String showId, String cShowId, String dName, String dLevel, String dParentName, String dParentShowId, String cName, String createUserName, Integer dSort, Integer dAvailableCount, Integer dUnAvailableCount, Integer childDeptCount, String extendField, String otherField) {
        this.id = id;
        this.showId = showId;
        this.cShowId = cShowId;
        this.dName = dName;
        this.dLevel = dLevel;
        this.dParentName = dParentName;
        this.dParentShowId = dParentShowId;
        this.cName = cName;
        this.createUserName = createUserName;
        this.dSort = dSort;
        this.dAvailableCount = dAvailableCount;
        this.dUnAvailableCount = dUnAvailableCount;
        this.childDeptCount = childDeptCount;
        this.extendField = extendField;
        this.otherField = otherField;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getCShowId() {
        return cShowId;
    }

    public void setCShowId(String cShowId) {
        this.cShowId = cShowId;
    }

    public String getDName() {
        return dName;
    }

    public void setDName(String dName) {
        this.dName = dName;
    }

    public String getDLevel() {
        return dLevel;
    }

    public void setDLevel(String dLevel) {
        this.dLevel = dLevel;
    }

    public String getDParentName() {
        return dParentName;
    }

    public void setDParentName(String dParentName) {
        this.dParentName = dParentName;
    }

    public String getDParentShowId() {
        return dParentShowId;
    }

    public void setDParentShowId(String dParentShowId) {
        this.dParentShowId = dParentShowId;
    }

    public String getCName() {
        return cName;
    }

    public void setCName(String cName) {
        this.cName = cName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Integer getDSort() {
        return dSort;
    }

    public void setDSort(Integer dSort) {
        this.dSort = dSort;
    }

    public Integer getDAvailableCount() {
        return dAvailableCount;
    }

    public void setDAvailableCount(Integer dAvailableCount) {
        this.dAvailableCount = dAvailableCount;
    }

    public Integer getDUnAvailableCount() {
        return dUnAvailableCount;
    }

    public void setDUnAvailableCount(Integer dUnAvailableCount) {
        this.dUnAvailableCount = dUnAvailableCount;
    }

    public Integer getChildDeptCount() {
        return childDeptCount;
    }

    public void setChildDeptCount(Integer childDeptCount) {
        this.childDeptCount = childDeptCount;
    }

    public String getExtendField() {
        return extendField;
    }

    public void setExtendField(String extendField) {
        this.extendField = extendField;
    }

    public String getOtherField() {
        return otherField;
    }

    public void setOtherField(String otherField) {
        this.otherField = otherField;
    }

}