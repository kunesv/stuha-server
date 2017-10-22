package net.stuha.notifications;

import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

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
        subscription.setId(UUID.randomUUID());
        return subscriptionRepository.save(subscription);
    }

    @Override
    public void sendNotifications(UUID conversationId, UUID userId) throws GeneralSecurityException, InterruptedException, JoseException, ExecutionException, IOException {
        final List<Subscription> subscriptions = subscriptionRepository.findSubscriptions(conversationId, userId);

        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }

        final PushService pushService = new PushService();

        for (Subscription subscription : subscriptions) {
            Notification notification = new Notification(subscription.getEndpoint(), getPublicKey(subscription.getKey()), subscription.getAuth(), "".getBytes());
            pushService.sendAsync(notification);
        }
    }


    @Override
    public SubscriptionConversation addConversation(final SubscriptionConversation subscriptionConversation) {

        subscriptionConversation.setId(UUID.randomUUID());

        return subscriptionConversationRepository.save(subscriptionConversation);
    }
}
