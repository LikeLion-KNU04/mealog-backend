package knulions.mealog.auth.oauth2.dto.response;

import java.util.Map;

public class GoogleResponse implements Oauth2Response{

    private final Map<String, Object> attributes;

    public GoogleResponse(Map<String, Object> attribute) {
        this.attributes = attribute;
    }

    @Override
    public String getProvider() {

        return "google";
    }

    /**
     * Provider 측에서 제공하는 사용자 구분 id 값
     */
    @Override
    public String getProviderId() {

        return attributes.get("sub").toString();
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
