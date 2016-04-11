package com.quaigon.threatsapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.inject.Inject;
import com.quaigon.threatsapp.R;
import com.quaigon.threatsapp.connection.AuthenticationRepository;
import com.quaigon.threatsapp.connection.ConnectionService;
import com.quaigon.threatsapp.connection.ServiceGenerator;
import com.quaigon.threatsapp.dto.Status;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VoteActivity extends RoboActivity {

    @InjectView(R.id.postVoteButton)
    private Button postVoteButton;

    @InjectView(R.id.voteRating)
    private RatingBar voteRatingBar;

    @InjectView(R.id.comment)
    private EditText commentEditText;

    @InjectExtra("uuid")
    private String threatUuid;

    @Inject
    private AuthenticationRepository authRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        postVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionService postButtonService = ServiceGenerator.createService(ConnectionService.class);
                postButtonService.addVote(String.valueOf(voteRatingBar.getNumStars()), threatUuid,authRepo.loadUserLogin(), commentEditText.getText().toString(),
                                            authRepo.loadToken().getToken())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new Subscriber<Status>() {
                            @Override
                            public void onCompleted() {
                                Ln.d("complete");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Ln.e(e);
                            }

                            @Override
                            public void onNext(Status status) {
                                Ln.d(status.whatsUp());
                            }
                        });
            }
        });
    }
}
