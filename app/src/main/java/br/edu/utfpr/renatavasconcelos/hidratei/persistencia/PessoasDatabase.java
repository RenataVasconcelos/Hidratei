package br.edu.utfpr.renatavasconcelos.hidratei.persistencia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import br.edu.utfpr.renatavasconcelos.hidratei.modelo.Pessoa;

@Database(entities = {Pessoa.class}, version = 1, exportSchema = false)
public abstract class PessoasDatabase extends RoomDatabase{

    public abstract PessoaDao getPessoaDao();

    private  static PessoasDatabase INSTANCE;

    public static PessoasDatabase getInstance(final Context context){

        if (INSTANCE == null){

            synchronized (PessoasDatabase.class){

                if (INSTANCE == null){

                    INSTANCE = Room.databaseBuilder(context,
                               PessoasDatabase.class,
                            "pessoas.db").allowMainThreadQueries().build();
                }
            }
        }

        return INSTANCE;
    };

}
