// TODO: Add licence header

package com.androidim;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidim.interfaces.IAppManager;
import com.androidim.services.IMService;
import com.ecml.R;

// TODO: Add javadoc
public class DeleteFriend extends Activity implements OnClickListener {

    private static Button mDeleteFriendButton;
    private static Button mCancelButton;
    private static EditText mFriendUserNameText;

    private static IAppManager mImService;

    private static final int TYPE_FRIEND_USERNAME = 0;
    private static final String LOG_TAG = "DeleteFriend";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.delete_friend);
        setTitle(getString(R.string.delete_friend));

        mDeleteFriendButton = (Button)findViewById(R.id.deleteFriend);
        mCancelButton = (Button)findViewById(R.id.cancel);
        mFriendUserNameText = (EditText)findViewById(R.id.newFriendUsername);

        if (mDeleteFriendButton != null) {
            mDeleteFriendButton.setOnClickListener(this);
        } else {
            Log.e(LOG_TAG, "onCreate: mDeleteFriendButton is null");
            throw new NullPointerException("onCreate: mDeleteFriendButton is null");
        }

        if (mCancelButton != null) {
            mCancelButton.setOnClickListener(this);
        } else {
            Log.e(LOG_TAG, "onCreate: mCancelButton is null");
            throw new NullPointerException("onCreate: mCancelButton is null");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent(this, IMService.class);
        if (mConnection != null) {
            bindService(intent, mConnection , Context.BIND_AUTO_CREATE);
        } else {
            Log.e(LOG_TAG, "onResume: mConnection is null");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mConnection != null) {
            unbindService(mConnection);
        } else {
            Log.e(LOG_TAG, "onResume: mConnection is null");
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mCancelButton) {
            finish();
        } else if (view == mDeleteFriendButton) {
            deleteFriend();
        } else {
            Log.e(LOG_TAG, "onClick: view clicked is unknown");
        }
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mImService = ((IMService.IMBinder)service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            if (mImService != null) {
                mImService = null;
            }

            Toast.makeText(DeleteFriend.this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
        }
    };

    // TODO: Remove deprecated method
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DeleteFriend.this);
        if (id == TYPE_FRIEND_USERNAME) {
            builder.setTitle(R.string.delete_friend)
                   .setMessage(R.string.type_friend_username)
                   .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int whichButton) {
                           // TODO
                       }
                   });
        }

        return builder.create();
     }

    private void deleteFriend() {
        if (mFriendUserNameText.length() > 0) {
            // TODO: A thread is really needed ?
            Thread thread = new Thread() {
                @Override
                public void run() {
                    // TODO: Please check if the request is successful and raise a error message if needed.
                    mImService.deleteFriendRequest(mFriendUserNameText.getText().toString());
                }
            };
            thread.start();

            // TODO: Show the toast only if the sent of the request is successful
            Toast.makeText(DeleteFriend.this, R.string.friend_deleted, Toast.LENGTH_SHORT).show();

            finish();
        } else {
            Log.e(LOG_TAG, "deleteFriend: username length (" + mFriendUserNameText.length() + ") is < 0");
            Toast.makeText(DeleteFriend.this, R.string.type_friend_username, Toast.LENGTH_LONG).show();
        }
    }
}
