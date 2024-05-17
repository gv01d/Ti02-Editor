package dao;

import model.Produto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO extends DAO {
	public ProdutoDAO() {
		super();
		conectar();
	}

	public void finalize() {
		close();
	}

	public boolean insert(Produto produto) {
		boolean status = false;
		try {
			System.out.println("INSERT INTO produto (nomecompleto, email, datanascimento, instrumento, descricao) "
					+ "VALUES ('" + produto.getNomeCompleto() + "', '"
					+ produto.getEmail() + "', ?"
					+ ", '" + produto.getInstrumento()
					+ "', '" + produto.getDescricao() + "')");
			String sql = "INSERT INTO produto (nomecompleto, email, datanascimento, instrumento, descricao) "
					+ "VALUES ('" + produto.getNomeCompleto() + "', '"
					+ produto.getEmail() + "', ?"
					+ ", '" + produto.getInstrumento()
					+ "', '" + produto.getDescricao() + "')";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setDate(1, Date.valueOf(produto.getDataNascimento()));
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {
			throw new RuntimeException(u);
		}
		return status;
	}

	public Produto get(int id) {
		Produto produto = null;

		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM produto WHERE id=" + id;
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				produto = new Produto(
						rs.getInt("id"),
						rs.getString("nomecompleto"),
						rs.getString("email"),
						rs.getDate("datanascimento").toLocalDate(),
						rs.getString("descricao"),
						rs.getString("instrumento"));
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return produto;
	}

	public List<Produto> get() {
		return get("");
	}

	public List<Produto> getOrderByID() {
		return get("id");
	}

	public List<Produto> getOrderByName() {

		return get("nomecompleto");
	}

	public List<Produto> getOrderByEmail() {
		return get("email");
	}

	private List<Produto> get(String orderBy) {
		List<Produto> produtos = new ArrayList<Produto>();

		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM produto" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				Produto p = new Produto(rs.getInt("id"), rs.getString("nomecompleto"), rs.getString("email"),
						rs.getDate("dataNascimento").toLocalDate(),
						rs.getString("instrumento"), rs.getString("descricao"));
				produtos.add(p);
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return produtos;
	}

	public boolean update(Produto produto) {
		boolean status = false;
		try {
			String sql = "UPDATE produto SET" + " nomecompleto = '" + produto.getNomeCompleto() + "', "
					+ "email = '" + produto.getEmail() + "', "
					+ "datanascimento = ?, "
					+ "instrumento = '" + produto.getInstrumento() + "', "
					+ "descricao = '" + produto.getDescricao()
					+ "' WHERE id = " + produto.getId();
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setDate(1, Date.valueOf(produto.getDataNascimento()));
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {
			throw new RuntimeException(u);
		}
		return status;
	}

	public boolean delete(int id) {
		boolean status = false;
		try {
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM produto WHERE id = " + id);
			st.close();
			status = true;
		} catch (SQLException u) {
			throw new RuntimeException(u);
		}
		return status;
	}
}