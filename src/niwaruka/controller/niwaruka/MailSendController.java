package niwaruka.controller.niwaruka;

import niwaruka.service.TagMailQueueService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class MailSendController extends Controller {
    TagMailQueueService s = new TagMailQueueService();

    @Override
    public Navigation run() throws Exception {

        s.dequeueAndSend(8);

        return null;
    }
}
