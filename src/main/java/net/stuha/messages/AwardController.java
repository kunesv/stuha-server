package net.stuha.messages;

import net.stuha.messages.awards.Standing;
import net.stuha.security.AuthorizationService;
import net.stuha.security.UnauthorizedRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RestController
public class AwardController {

    private final ConversationService conversationService;
    private final AwardService awardService;

    @Autowired
    public AwardController(ConversationService conversationService, AwardService awardService) {
        Assert.notNull(conversationService);
        Assert.notNull(awardService);

        this.conversationService = conversationService;
        this.awardService = awardService;
    }

    @RequestMapping(value = "/ranicek/overall/{conversationId}", method = RequestMethod.GET)
    public List<Standing> ranicekOverallStandings(@PathVariable UUID conversationId, HttpServletRequest request) throws UnauthorizedRequestException {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        if (!conversationService.userHasConversation(conversationId, userId)) {
            throw new UnauthorizedRequestException();
        }

        return awardService.ranicekOverallStandings(conversationId);
    }
}
