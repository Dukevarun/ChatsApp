package uk.ac.tees.w9218308.chatsapp.Utils;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class SendNotification {

    public SendNotification(String message, String heading, String notificationKey) {

        notificationKey = "1e3608cd-f75d-4296-90f9-cb487e468637";
        try {
            JSONObject notificationContent = new JSONObject(
                    "{'contents': {'en':'" + message + "'}," +
                            "'include_player_ids': ['" + notificationKey + "']" +
                            "'headings':{'en': '" + heading + "'}}");
            OneSignal.postNotification(notificationContent, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
