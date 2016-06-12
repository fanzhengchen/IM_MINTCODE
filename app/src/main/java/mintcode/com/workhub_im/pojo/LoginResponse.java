package mintcode.com.workhub_im.pojo;

import java.util.List;

/**
 * Created by mark on 16-6-8.
 */
public class LoginResponse {


    /**
     * IsSuccess : true
     * ResCode : 8200
     * Reason :
     */

    private HeaderBean Header;
    /**
     * response : {"Data":{"validatorToken":"YYG77y8ndysr3vNB","isIpLogined":1,"companyList":[{"showId":"35b11f42f4522d8924","cCode":"mt","cName":"Mintcode"},{"showId":"evJxevzeOJcBrlAj","cCode":"test","cName":"test"}]},"TotalCount":1,"IsSuccess":true,"Code":8200,"Reason":""}
     */

    private BodyBean Body;

    public HeaderBean getHeader() {
        return Header;
    }

    public void setHeader(HeaderBean Header) {
        this.Header = Header;
    }

    public BodyBean getBody() {
        return Body;
    }

    public void setBody(BodyBean Body) {
        this.Body = Body;
    }

    public static class BodyBean {
        /**
         * Data : {"validatorToken":"YYG77y8ndysr3vNB","isIpLogined":1,"companyList":[{"showId":"35b11f42f4522d8924","cCode":"mt","cName":"Mintcode"},{"showId":"evJxevzeOJcBrlAj","cCode":"test","cName":"test"}]}
         * TotalCount : 1
         * IsSuccess : true
         * Code : 8200
         * Reason :
         */

        private ResponseBean response;

        public ResponseBean getResponse() {
            return response;
        }

        public void setResponse(ResponseBean response) {
            this.response = response;
        }

        public static class ResponseBean {
            /**
             * validatorToken : YYG77y8ndysr3vNB
             * isIpLogined : 1
             * companyList : [{"showId":"35b11f42f4522d8924","cCode":"mt","cName":"Mintcode"},{"showId":"evJxevzeOJcBrlAj","cCode":"test","cName":"test"}]
             */

            private DataBean Data;
            private int TotalCount;
            private boolean IsSuccess;
            private int Code;
            private String Reason;

            public DataBean getData() {
                return Data;
            }

            public void setData(DataBean Data) {
                this.Data = Data;
            }

            public int getTotalCount() {
                return TotalCount;
            }

            public void setTotalCount(int TotalCount) {
                this.TotalCount = TotalCount;
            }

            public boolean isIsSuccess() {
                return IsSuccess;
            }

            public void setIsSuccess(boolean IsSuccess) {
                this.IsSuccess = IsSuccess;
            }

            public int getCode() {
                return Code;
            }

            public void setCode(int Code) {
                this.Code = Code;
            }

            public String getReason() {
                return Reason;
            }

            public void setReason(String Reason) {
                this.Reason = Reason;
            }

            public static class DataBean {
                private String validatorToken;
                private int isIpLogined;
                /**
                 * showId : 35b11f42f4522d8924
                 * cCode : mt
                 * cName : Mintcode
                 */

                private List<CompanyListBean> companyList;

                public String getValidatorToken() {
                    return validatorToken;
                }

                public void setValidatorToken(String validatorToken) {
                    this.validatorToken = validatorToken;
                }

                public int getIsIpLogined() {
                    return isIpLogined;
                }

                public void setIsIpLogined(int isIpLogined) {
                    this.isIpLogined = isIpLogined;
                }

                public List<CompanyListBean> getCompanyList() {
                    return companyList;
                }

                public void setCompanyList(List<CompanyListBean> companyList) {
                    this.companyList = companyList;
                }

                public static class CompanyListBean {
                    private String showId;
                    private String cCode;
                    private String cName;

                    public String getShowId() {
                        return showId;
                    }

                    public void setShowId(String showId) {
                        this.showId = showId;
                    }

                    public String getCCode() {
                        return cCode;
                    }

                    public void setCCode(String cCode) {
                        this.cCode = cCode;
                    }

                    public String getCName() {
                        return cName;
                    }

                    public void setCName(String cName) {
                        this.cName = cName;
                    }
                }
            }
        }
    }
}
