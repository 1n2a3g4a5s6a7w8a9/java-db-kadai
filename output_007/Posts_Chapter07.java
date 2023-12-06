package kadai_007;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Posts_Chapter07 {

	public static void main(String[] args) {
		Connection con = null;
		PreparedStatement statement = null;
		Statement statement2 = null;
		
		//ユーザーリスト
		String[][] userList = {
			{"1003","2023-02-08","昨日の夜は徹夜でした・・","13"},
			{"1002","2023-02-08","お疲れ様です！","12"},
			{"1003","2023-02-09","今日も頑張ります！","18"},
			{"1001","2023-02-09","油断は禁物ですよ！","17"},
			{"1002","2023-02-10","明日から連休ですね","20"},
		};
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost/challenge_java",
					"root",
					"*****");
			
			System.out.println("データベース接続成功："+ con);
			
			//投稿データを追加する
			String sql = "INSERT INTO posts (user_id, posted_at, post_content, likes)"
					+ " VALUES (?, ?, ?, ?);";
			statement = con.prepareStatement(sql);
			
			//リストの1行目から順番に読み込む
			int rowCnt = 0;
			int isrtCnt = 0;
			System.out.println("レコード追加を実行します");
			for (int i = 0; i < userList.length; i++) {
				//sqlクエリの？部分をリストのデータに置換
				statement.setString(1, userList[i][0]);//ユーザーid
				statement.setString(2, userList[i][1]);//投稿日時
				statement.setString(3, userList[i][2]);//投稿内容
				statement.setString(4, userList[i][3]);//いいね数
				
				//クエリの実行
				rowCnt = statement.executeUpdate();
				
				isrtCnt = isrtCnt +1;
				}
			System.out.println(isrtCnt + "件のレコードが追加されました");
			//投稿データを検索する
			statement2 = con.createStatement();
			sql = "SELECT posted_at, post_content, likes "
					+ "FROM posts WHERE user_id = 1002;";

			//検索結果を抽出・表示
			ResultSet result = statement2.executeQuery(sql);
			System.out.println("ユーザーIDが1002のレコードを検索しました。");
			//結果を抽出
			while(result.next()) {
				Date posted_at =  result.getDate("posted_at");
				String post_content = result.getString("post_content");
				int likes = result.getInt("likes");
				System.out.println(result.getRow() + "件目:投稿日時="+ posted_at 
						+ "/投稿内容=" + post_content + "/いいね数=" + likes ); 
			}
			} catch(SQLException e) {
				System.out.println("エラーの発生："+e.getMessage());
			} finally {
				//私用したオブジェクトを開放
				if ( statement != null ) {
					try { statement.close();} catch(SQLException ignore) {}
				}
				if ( statement2 != null ) {
					try { statement2.close();} catch(SQLException ignore) {}
				}
				if ( con != null) {
					try { con.close();} catch(SQLException ignore) {}
				}
		}	
	}
}
