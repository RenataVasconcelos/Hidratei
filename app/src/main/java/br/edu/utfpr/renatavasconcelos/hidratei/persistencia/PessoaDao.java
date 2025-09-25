package br.edu.utfpr.renatavasconcelos.hidratei.persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.edu.utfpr.renatavasconcelos.hidratei.modelo.Pessoa;

@Dao
public interface PessoaDao {

    @Insert
    long insert(Pessoa pessoa);

    @Delete
    int delete(Pessoa pessoa);

    @Update
    int update(Pessoa pessoa);

    @Query("SELECT * FROM pessoa WHERE id =:id")
    Pessoa queryForId(long id);

    @Query("SELECT * FROM pessoa ORDER BY nome ASC")
    List<Pessoa> queryAllAscending();

    @Query("SELECT * FROM pessoa ORDER BY nome DESC")
    List<Pessoa> queryAllDownward();
}
