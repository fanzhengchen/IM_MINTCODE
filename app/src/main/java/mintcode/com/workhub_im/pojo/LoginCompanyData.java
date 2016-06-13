package mintcode.com.workhub_im.pojo;

import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.bind.ArrayTypeAdapter;

import java.util.ArrayList;

/**
 * Created by mark on 16-6-13.
 */
public class LoginCompanyData {
    @SerializedName("validatorToken")
    private String validatorToken;
    @SerializedName("isIpLogined")
    private String isIpLogin;
    @SerializedName("companyList")
    private ArrayList<CompanyEntity> entities;

    public String getValidatorToken() {
        return validatorToken;
    }

    public void setValidatorToken(String validatorToken) {
        this.validatorToken = validatorToken;
    }

    public String getIsIpLogin() {
        return isIpLogin;
    }

    public void setIsIpLogin(String isIpLogin) {
        this.isIpLogin = isIpLogin;
    }

    public ArrayList<CompanyEntity> getEntities() {
        return entities;
    }

    public void setEntities(ArrayList<CompanyEntity> entities) {
        this.entities = entities;
    }
}
