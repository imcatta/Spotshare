package it.catta.spotshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import it.catta.spotshare.ParseEntity.Comment;


public class CommentArrayAdapter extends ArrayAdapter<Comment> {

    private final Context context;
    private final Comment[] values;

    public CommentArrayAdapter(Context context, Comment[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.comment_list_item, parent, false);

        final TextView authorTextView = (TextView) rowView.findViewById(R.id.commentAuthorTextView);
        TextView contentTextView = (TextView) rowView.findViewById(R.id.commentContentTextView);

        Comment comment = values[position];
        comment.getCreatedBy().fetchIfNeededInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    authorTextView.setText(user.getUsername());
                }
            }
        });
        contentTextView.setText(comment.getContent());

        return rowView;
    }

}
