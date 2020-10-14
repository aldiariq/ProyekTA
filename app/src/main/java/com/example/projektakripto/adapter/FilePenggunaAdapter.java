package com.example.projektakripto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projektakripto.R;
import com.example.projektakripto.model.FilePengguna;
import com.example.projektakripto.utils.OnDeleteClickListener;
import com.example.projektakripto.utils.OnDownloadClickListener;

import java.util.ArrayList;

public class FilePenggunaAdapter extends RecyclerView.Adapter<FilePenggunaAdapter.ViewHolder> {
    Context context;
    ArrayList<FilePengguna> listFilepengguna;

    OnDownloadClickListener onDownloadClickListener;
    OnDeleteClickListener onDeleteClickListener;

    public FilePenggunaAdapter(Context context){
        this.context = context;
        listFilepengguna = new ArrayList<>();
    }

    public void add(FilePengguna filePengguna){
        listFilepengguna.add(filePengguna);
        notifyItemInserted(listFilepengguna.size() - 1);
    }

    public void addAll(ArrayList<FilePengguna> filePenggunas){
        for (FilePengguna filePengguna : filePenggunas){
            add(filePengguna);
        }
    }

    public void clear(){
        listFilepengguna.clear();
    }

    public void setOnDownloadClickListener(OnDownloadClickListener onDownloadClickListener){
        this.onDownloadClickListener = onDownloadClickListener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener){
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public FilePengguna getFilepengguna(int position){
        return listFilepengguna.get(position);
    }

    public void removeFilepengguna(int position){
        if (position >= 0 && position < listFilepengguna.size()){
            listFilepengguna.remove(position);
            notifyItemRemoved(position);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(listFilepengguna.get(position));
    }

    @Override
    public int getItemCount() {
        return listFilepengguna.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNamafile;
        ImageView imgDownloadfile, imgDeletefile;

        public ViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.itemfile_penyimpanan, itemView, false));
            initView();

            imgDownloadfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDownloadClickListener.onDownloadClick(getAdapterPosition());
                }
            });

            imgDeletefile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClickListener.onDeleteClick(getAdapterPosition());
                }
            });
        }

        public void bind(FilePengguna filePengguna){
            tvNamafile.setText(filePengguna.getNama_file());
        }

        private void initView(){
            tvNamafile = (TextView) itemView.findViewById(R.id.tv_namafile_itemfile);
            imgDownloadfile = (ImageView) itemView.findViewById(R.id.img_download_itemfile);
            imgDeletefile = (ImageView) itemView.findViewById(R.id.img_delete_itemfile);
        }
    }
}
