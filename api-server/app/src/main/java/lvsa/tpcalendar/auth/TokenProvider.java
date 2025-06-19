package lvsa.tpcalendar.auth;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import lvsa.tpcalendar.utils.IOUtils;
import lvsa.tpcalendar.utils.PropsService;

public class TokenProvider implements AutoCloseable {
    private final String issuer;
    private final Algorithm algorithm;
    private final long tokenExpiryMs;
    private final String keyId;
    private final Properties props = new PropsService().getRsaKeys();
    
    public TokenProvider() throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.issuer = "three-phase-calendar";
        this.keyId = new PropsService().getRsaKeys().getProperty("jwt.key.id");

        weirdNightlyPropFix();
        byte[] decodedPubKey = Base64.getDecoder().decode(props.get("jwt.pub.key").toString());
        byte[] decodedPrivKey = Base64.getDecoder().decode(props.get("jwt.priv.key").toString());
        X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(decodedPubKey);
        PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(decodedPrivKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKey rsaPubKey = (RSAPublicKey) kf.generatePublic(pubSpec);
        RSAPrivateKey rsaPrivKey = (RSAPrivateKey) kf.generatePrivate(privSpec);

        this.algorithm = Algorithm.RSA256(rsaPubKey, rsaPrivKey);
        this.tokenExpiryMs = 86_400_000; // 24 hours!
    }

    private void weirdNightlyPropFix() {
        props.setProperty("jwt.key.id", IOUtils.readAnyResource("key_id"));
        props.setProperty("jwt.pub.key", IOUtils.readAnyResource("public_key.pem"));;
        props.setProperty("jwt.priv.key", IOUtils.readAnyResource("private_key.pem"));
        props.setProperty("jwt.pub.key", props.get("jwt.pub.key").toString()
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replaceAll("\\s+", ""));
        props.setProperty("jwt.priv.key", props.get("jwt.priv.key").toString()
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replaceAll("\\s+", ""));
    }

    /**
     * Create a JWT token with RSA256 algorithm from the public & private key.
     */
    public String createJWTToken(int userId) {
        return JWT.create()
            .withIssuer(issuer)
            .withSubject(String.valueOf(userId))
            .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpiryMs))
            .withKeyId(keyId)
            .withJWTId(UUID.randomUUID().toString())
            .sign(algorithm);
    }

    public DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm)
            .withIssuer(issuer)
            .build();
        
        return verifier.verify(token);
    }

    @Override
    public void close() throws Exception { return; }
        
}