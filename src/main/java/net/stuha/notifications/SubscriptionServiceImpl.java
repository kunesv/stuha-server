package net.stuha.notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.stuha.messages.Message;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Utils;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Value("${push.public}")
    private String publicKey;

    @Value("${push.private}")
    private String privateKey;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private SubscriptionConversationRepository subscriptionConversationRepository;

    private PublicKey getPublicKey(byte[] key) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        KeyFactory kf = KeyFactory.getInstance("ECDH", BouncyCastleProvider.PROVIDER_NAME);
        ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256r1");
        ECPoint point = ecSpec.getCurve().decodePoint(key);
        ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, ecSpec);

        return kf.generatePublic(pubSpec);
    }

    @Override
    public Subscription subscribe(Subscription subscription) {
        final Subscription persistedSubscription = subscriptionRepository.findFirstByEndpoint(subscription.getEndpoint());
        if (persistedSubscription != null) {
            return persistedSubscription;
        }

        subscription.setId(UUID.randomUUID());
        return subscriptionRepository.save(subscription);
    }

    @Override
    public void sendNotifications(UUID conversationId, UUID userId, Message message) throws GeneralSecurityException, InterruptedException, JoseException, ExecutionException, IOException {
        final List<Subscription> subscriptions = subscriptionRepository.findSubscriptions(conversationId, userId);

        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }

        final PushService pushService = new PushService();
        pushService.setPublicKey(Utils.loadPublicKey(publicKey));
        pushService.setPrivateKey(Utils.loadPrivateKey(privateKey));

        for (Subscription subscription : subscriptions) {
            Notification notification = new Notification(subscription.getEndpoint(), getPublicKey(subscription.getKey()), subscription.getAuth(), new ObjectMapper().writeValueAsBytes(message));
            pushService.sendAsync(notification);
        }
    }


    @Override
    public SubscriptionConversation addConversation(final SubscriptionConversation subscriptionConversation) {

        subscriptionConversation.setId(UUID.randomUUID());

        return subscriptionConversationRepository.save(subscriptionConversation);
    }

    @Override
    public List<SubscriptionConversation> getAll(String endpoint, UUID userId) {
        return subscriptionConversationRepository.getAllForUserAndEndpoint(endpoint, userId);
    }

    @Override
    public void removeConversation(UUID conversationId, UUID userId) {
        final SubscriptionConversation subscriptionConversation = subscriptionConversationRepository.findFirstByConversationIdAndUserId(conversationId, userId);
        subscriptionConversationRepository.delete(subscriptionConversation.getId());
    }
}
