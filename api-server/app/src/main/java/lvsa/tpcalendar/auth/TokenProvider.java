package lvsa.tpcalendar.auth;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.RSAKeyProvider;

public class TokenProvider {
    private final RSAPublicKey rsaPublicKey = null;
    private final RSAPrivateKey rsaPrivateKey = null;
    private final String rsaPrivateKeyId = "";
    private final RSAKeyProvider keyProvider = new RSAKeyProvider() {
			@Override
			public RSAPublicKey getPublicKeyById(String keyId) {
                return rsaPublicKey; // How do we obtain it?
			}

			@Override
			public RSAPrivateKey getPrivateKey() {
                return rsaPrivateKey;
			}

			@Override
			public String getPrivateKeyId() {
                return rsaPrivateKeyId;
			}
    };

    public void createJWTToken() {
        try {
            Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
            String token = JWT.create()
                .withIssuer("auth0")
                .sign(algorithm);
            
        } catch (JWTCreationException jwtce) {
            jwtce.printStackTrace();
        }
    }
}