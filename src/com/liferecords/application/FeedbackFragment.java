package com.liferecords.application;

import com.parse.ParseUser;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedbackFragment extends Fragment {

	private EditText editSubject,editContent;
	private Button sendButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.feedback_layout, container,false);
		editSubject = (EditText) view.findViewById(R.id.edittext_subject);
		editContent = (EditText) view.findViewById(R.id.edittext_content);
		sendButton = (Button) view.findViewById(R.id.button_feedback);
		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		sendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkFeedbackErrors();
				sendEmail();
			}
		});
		super.onActivityCreated(savedInstanceState);
	}
	
	private void sendEmail(){
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL, new String[]{"liferecordsapp@gmail.com"});
		i.putExtra(Intent.EXTRA_SUBJECT, editSubject.getText().toString());
		i.putExtra(Intent.EXTRA_TEXT, editContent.getText().toString());
		try {
			startActivity(Intent.createChooser(i, "Send feedback.."));
		} catch (Exception e) {
			Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
	}
	private void checkFeedbackErrors() {
		boolean validationError = false;
		StringBuilder validationErrorMessage = new StringBuilder(getResources()
				.getString(R.string.error_intro));
		if (isEmpty(editSubject)) {
			validationError = true;
			validationErrorMessage.append(getResources().getString(
					R.string.error_blank_subject));
		}
		if (isEmpty(editContent)) {
			if (validationError) {
				validationErrorMessage.append(getResources().getString(
						R.string.error_join));
			}
			validationError = true;
			validationErrorMessage.append(getResources().getString(
					R.string.error_blank_content));
		}
		validationErrorMessage.append(getResources().getString(
				R.string.error_end));

		if (validationError) {
			Toast.makeText(getActivity(),
					validationErrorMessage.toString(), Toast.LENGTH_LONG)
					.show();
			return;
		}
	}
	
	private boolean isEmpty(EditText etText) {
		if (etText.getText().toString().trim().length() > 0) {
			return false;
		} else {
			return true;
		}
	}
}
