package castinder.cyrax.com.castindex.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import castinder.cyrax.com.castindex.R;

/**
 * Created by ignacioainolrivera on 09-05-18.
 */

public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ChatViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

    }
}