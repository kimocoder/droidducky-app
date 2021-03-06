package com.draguve.droidducky;

/**
 * Created by Draguve on 1/3/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;


public class ScriptsAdapter extends RecyclerView.Adapter<ScriptsAdapter.MyViewHolder> {

    private List<Script> scriptList;
    private Activity mainActivityContext;
    private ScriptsManager db;

    public ScriptsAdapter(List<Script> scriptList, Context mainActivityContext) {
        this.scriptList = scriptList;
        this.mainActivityContext = (Activity) mainActivityContext;
        db = new ScriptsManager(mainActivityContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.script_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int result = 1;
                Intent codeEditorIntent = new Intent(v.getContext(), CodeEditor.class);
                codeEditorIntent.putExtra("idSelected", scriptList.get(position).getID());
                codeEditorIntent.putExtra("editingMode", 0);
                mainActivityContext.startActivityForResult(codeEditorIntent, result);
            }
        });
        holder.run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //scriptList.get(position).executeCode(mainActivityContext);
                final int result = 1;
                Intent codeEditorIntent = new Intent(v.getContext(), ExecuterActivity.class);
                codeEditorIntent.putExtra("idSelected", scriptList.get(position).getID());
                codeEditorIntent.putExtra("currentMode", 0);
                mainActivityContext.startActivityForResult(codeEditorIntent, result);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(mainActivityContext)
                        .title(R.string.delete_script_dialog)
                        .positiveText(R.string.delete_dialog)
                        .negativeText(R.string.cancel_dialog)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                db.deleteScript(scriptList.get(position).getID());
                                updateScriptList(db.getAllScripts());
                            }
                        })
                        .show();
            }
        });
        Script script = scriptList.get(position);
        holder.title.setText(script.getName());
        holder.genre.setText(script.getLang().toUpperCase());
    }

    @Override
    public int getItemCount() {
        return scriptList.size();
    }

    public void updateScriptList(List<Script> scripts) {
        this.scriptList.clear();
        this.scriptList.addAll(scripts);
        this.notifyDataSetChanged();
    }

    public List<Script> getCurrentList() {
        return scriptList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, genre;
        public Button run, delete;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            genre = view.findViewById(R.id.genre);
            run = view.findViewById(R.id.list_run);
            delete = view.findViewById(R.id.list_delete);
        }
    }
}
