package com.ryan.ryan.abxstewards;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This class inflates the DialogFragment that displays the general information about the app,
 * such as what its about and who to contact for assistance
 */
public class MoreInfoAlertDialogFragment extends DialogFragment {

    private LinearLayout edurti_about_header;
    private ImageView edurti_about_header_arrow;
    private TextView edurti_about_text;

    private LinearLayout edurti_admin_contact_info_header;
    private ImageView edurti_admin_contact_info_header_arrow;
    private TextView edurti_admin_contact_info_text;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.edurti_more_info, null);

        builder.setView(view)
                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        edurti_about_header = (LinearLayout) view.findViewById(R.id.edurti_about_header);
        edurti_about_header_arrow = (ImageView) view.findViewById(R.id.edurti_about_header_arrow);
        edurti_about_text = (TextView) view.findViewById(R.id.edurti_about_text);

        edurti_admin_contact_info_header =
                (LinearLayout) view.findViewById(R.id.edurti_admin_contact_info_header);
        edurti_admin_contact_info_header_arrow =
                (ImageView) view.findViewById(R.id.edurti_admin_contact_info_header_arrow);
        edurti_admin_contact_info_text =
                (TextView) view.findViewById(R.id.edurti_admin_contact_info_text);

        // On click, toggle the visibility of the edurti_about_text text element
        edurti_about_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edurti_about_header_arrow.animate().rotationBy(180).setDuration(250).start();

                if(edurti_about_text.getVisibility() == edurti_about_text.VISIBLE){
                    edurti_about_text.setVisibility(edurti_about_text.GONE);

                }
                else{
                    edurti_about_text.setVisibility(edurti_about_text.VISIBLE);

                }

            }

        });

        // On click, toggle the visibility of the edurti_admin_contact_info_text text element
        edurti_admin_contact_info_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edurti_admin_contact_info_header_arrow
                        .animate().rotationBy(180).setDuration(250).start();

                if(edurti_admin_contact_info_text.getVisibility()
                        == edurti_admin_contact_info_text.VISIBLE){
                    edurti_admin_contact_info_text
                            .setVisibility(edurti_admin_contact_info_text.GONE);

                }
                else{
                    edurti_admin_contact_info_text
                            .setVisibility(edurti_admin_contact_info_text.VISIBLE);

                }

            }

        });

        return builder.create();


    }

}
