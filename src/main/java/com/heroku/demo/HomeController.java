package com.heroku.demo;

import com.heroku.demo.message.Message;
import com.heroku.demo.message.MessageRepository;
import com.heroku.demo.order.Buy;
import com.heroku.demo.order.BuyRepository;
import com.heroku.demo.photo.Photo;
import com.heroku.demo.photo.PhotoRepository;
import com.heroku.demo.review.Review;
import com.heroku.demo.review.ReviewRepository;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Contact;
import com.mailjet.client.resource.Email;
import org.joda.time.LocalTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private MessageRepository messageRepository;
    private ReviewRepository reviewRepository;
    private BuyRepository buyRepository;
    private PhotoRepository photoRepository;


    private static final Logger logger = LoggerFactory
            .getLogger(HomeController.class);

    @Autowired
    public HomeController(MessageRepository pRepository, ReviewRepository reviewRepository,
                          BuyRepository buyRepository, PhotoRepository photoRepository) {

        this.buyRepository = buyRepository;
        this.messageRepository = pRepository;
        this.reviewRepository = reviewRepository;
        this.photoRepository = photoRepository;

    }


    @RequestMapping("403")
    public String access403() {
        return "403";
    }

    @RequestMapping("upload")
    public String upload() {
        return "upload";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping("index")
    public String index2() {
        return index();
    }

    @RequestMapping("sendemail")
    @ResponseBody
    private String sendMail() throws MailjetSocketTimeoutException, MailjetException {
        MailjetRequest email;
        JSONArray recipients;
        MailjetResponse response;
        MailjetClient client = new MailjetClient("489ff3e95ebe1a6a3303dbd79ec3777f", "0be4f9f8ede6f035f85fd4393875f32d");

        recipients = new JSONArray()
                .put(new JSONObject().put(Contact.EMAIL, "dima-vers0@rambler.ru"));

        email = new MailjetRequest(Email.resource)
                .property(Email.FROMNAME, "Excursium")
                .property(Email.FROMEMAIL, "support@excursium.me")
                .property(Email.SUBJECT, "Subject")
                .property(Email.TEXTPART, "Java is coming!...")
                .property(Email.RECIPIENTS, recipients)
                .property(Email.MJCUSTOMID, "JAVA-Email");

        response = client.post(email);
        return response.getData() + " " + response.getStatus();
    }

    @RequestMapping("addmsg")
    @ResponseBody
    public String insertMsg(ModelMap model,
                            @ModelAttribute("msg") String message,
                            @ModelAttribute("event_id") int eventId,
                            @ModelAttribute("user_id") int userId,
                            BindingResult result) {
        Message msg = new Message(userId, eventId, new LocalTime().toDateTimeToday().toString(), message);
        if (!result.hasErrors()) {
            //person.setWhat(3);
            messageRepository.save(msg);
        }
        return msg.toString();
    }


    @RequestMapping("/getmsgs")
    @ResponseBody
    public String getMsgs() {
        ArrayList<String> arrayList = new ArrayList<>();
        List<Message> messages = messageRepository.findAll();
        for (Message m : messages) {
            arrayList.add(m.toString());
        }

        StringBuilder stringBuilder = new StringBuilder("{ \"messages\": [");

        for (int i = 0; i < arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size() - i > 1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }



    @RequestMapping("/addreview")
    @ResponseBody
    public String insertReview(ModelMap model,
                               @ModelAttribute("data") String data,
                               @ModelAttribute("image_url") String imageUrl,
                               @ModelAttribute("user_id") int userId,
                               @ModelAttribute("event_id") int eventId,
                               @ModelAttribute("rate") int rate,
                               BindingResult result) {

        Review review = new Review(data, imageUrl, "TIME", userId, eventId, rate);
        if (!result.hasErrors()) {
            //person.setWhat(3);
            reviewRepository.save(review);
        }
        return review.toString();
    }


    @RequestMapping("/getreviews")
    @ResponseBody
    public String getReviews() {
        ArrayList<String> arrayList = new ArrayList<>();
        List<Review> persons = reviewRepository.findAll();
        for (Review r : persons) {
            arrayList.add(r.toString());
        }

        StringBuilder stringBuilder = new StringBuilder("{ \"reviews\": [");

        for (int i = 0; i < arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size() - i > 1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }

    @RequestMapping("/addorder")
    @ResponseBody
    public String insertOrder(ModelMap model,
                              @ModelAttribute("price") int price,
                              @ModelAttribute("tourist_id") int touristId,
                              @ModelAttribute("event_id") int eventId,
                              BindingResult result) {

        Buy buy = new Buy(eventId, touristId, price, "TIME");
        if (!result.hasErrors()) {
            //person.setWhat(3);
            buyRepository.save(buy);
        }
        return buy.toString();
    }


    @RequestMapping("/getorders")
    @ResponseBody
    public String getOrders() {
        ArrayList<String> arrayList = new ArrayList<>();
        List<Buy> buys = buyRepository.findAll();
        for (Buy o : buys) {
            arrayList.add(o.toString());
        }

        StringBuilder stringBuilder = new StringBuilder("{ \"buys\": [");

        for (int i = 0; i < arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size() - i > 1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }


    @RequestMapping("addphoto")
    @ResponseBody
    public String insertPhoto(ModelMap model,
                              @ModelAttribute("data") String data,
                              @ModelAttribute("event_id") int eventId,
                              BindingResult result) {

        Photo photo = new Photo(eventId, data);
        if (!result.hasErrors()) {
            //person.setWhat(3);
            photoRepository.save(photo);
        }
        return photo.toString();
    }

    @RequestMapping("getphotos")
    @ResponseBody
    public String getPhotos() {
        ArrayList<String> arrayList = new ArrayList<>();
        List<Photo> buys = photoRepository.findAll();
        for (Photo o : buys) {
            arrayList.add(o.toString());
        }

        StringBuilder stringBuilder = new StringBuilder("{ \"photos\": [");

        for (int i = 0; i < arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size() - i > 1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }

    @RequestMapping(value = "uploadMultipleFile", method = RequestMethod.POST)
    public @ResponseBody
    String uploadMultipleFileHandler(@RequestParam("file") MultipartFile[] files) {

        StringBuilder message = new StringBuilder();
        for (MultipartFile file : files) {
            String name = file.getOriginalFilename();
            try {
                byte[] bytes = file.getBytes();

                // Creating the directory to store file
                String rootPath = System.getProperty("catalina.home");
                File dir = new File(rootPath + File.separator + "tmpFiles");
                if (!dir.exists())
                    dir.mkdirs();

                // Create the file on server
                File serverFile = new File(dir.getAbsolutePath()
                        + File.separator + name);
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                logger.info("Server File Location="
                        + serverFile.getAbsolutePath());

                message.append("You successfully uploaded file=").append(name).append("<br />");
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        }
        return message.toString();
    }

}
