package edu.ucsb.cs56.projects.androidapp.smokesignals.api.commands;

import android.content.Context;
import android.content.Intent;

import edu.ucsb.cs56.projects.androidapp.smokesignals.RingActivity;
import edu.ucsb.cs56.projects.androidapp.smokesignals.api.Command;
import edu.ucsb.cs56.projects.androidapp.smokesignals.api.commands.validators.NArgValidator;

/**
 * Created by ankushrayabhari on 11/4/17.
 */

public class RingCommand extends NArgValidator implements Command {
    public RingCommand() {
        super(0);
    }

    @Override
    public String getUsage() {
        return "//ring";
    }

    public String execute(Context context, String[] args) {
        Intent ringIntent = new Intent(context, RingActivity.class);
        ringIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(ringIntent);

        return "Phone will now ring for the next 2 minutes!";
    }
}
