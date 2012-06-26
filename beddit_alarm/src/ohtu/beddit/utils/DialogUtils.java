package ohtu.beddit.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;


public class DialogUtils {

    /**
     * Creates an {@link AlertDialog} with given message and one button with given text, which when pressed finishes the {@link Activity}.
     */
    public static void createActivityClosingDialog(final Activity activity, String message, String buttonText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                activity.finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
