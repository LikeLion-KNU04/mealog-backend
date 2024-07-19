package knulions.mealog.auth.oauth2.dto.response;

import java.util.Map;

public class NaverResponse implements Oauth2Response{
    private final Map<String, Object> attributes;

    public NaverResponse(Map<String, Object> attribute) {
        this.attributes = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    /**
     * Provider 측에서 제공하는 사용자 구분 id 값
     */
    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }
}
