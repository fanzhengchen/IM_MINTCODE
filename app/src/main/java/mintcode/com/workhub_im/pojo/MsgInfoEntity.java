package mintcode.com.workhub_im.pojo;

/**
 * Created by JulyYu on 2016/6/23.
 */
public class MsgInfoEntity {

    private String title;
    private long start;
    private long end;
    private long deadline;
    private String id;
    private int fee;
    private int isDeadlineAllday;
    private int isTimeslotAllday;
    private String backup;
    private String approvalType;
    private String approvalShowID;
    private String approvalStatus;
    private String comment;
    private String reason;
    private String transUserName;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getIsDeadlineAllday() {
        return isDeadlineAllday;
    }

    public void setIsDeadlineAllday(int isDeadlineAllday) {
        this.isDeadlineAllday = isDeadlineAllday;
    }

    public int getIsTimeslotAllday() {
        return isTimeslotAllday;
    }

    public void setIsTimeslotAllday(int isTimeslotAllday) {
        this.isTimeslotAllday = isTimeslotAllday;
    }

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }

    public String getApprovalType() {
        return approvalType;
    }

    public void setApprovalType(String approvalType) {
        this.approvalType = approvalType;
    }

    public String getApprovalShowID() {
        return approvalShowID;
    }

    public void setApprovalShowID(String approvalShowID) {
        this.approvalShowID = approvalShowID;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTransUserName() {
        return transUserName;
    }

    public void setTransUserName(String transUserName) {
        this.transUserName = transUserName;
    }
}
