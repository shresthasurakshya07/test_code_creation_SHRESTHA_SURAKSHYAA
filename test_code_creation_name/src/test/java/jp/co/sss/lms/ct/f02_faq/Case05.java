package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.*;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト よくある質問機能
 * ケース05
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース05 キーワード検索 正常系")
public class Case05 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		//		トップページURLにアクセスする、ログイン画面が表示される
		goTo("http://localhost:8080/lms");
		visibilityTimeout(By.id("login-title"), 5);
		assertEquals("ログイン", webDriver.findElement(By.id("login-title")).getText());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		//初回ログイン済みの受講生ユーザーでログイン
		webDriver.findElement(By.id("loginId")).sendKeys("StudentAA02");
		webDriver.findElement(By.id("password")).sendKeys("StudentAA021");
		//		コース詳細画面に遷移する、「ログイン」をクリック
		webDriver.findElement(
				By.cssSelector("input[type='submit'][value='ログイン']")).click();
		//　正解　ログイン　の　エビデンス
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {

		// 「機能」をクリックしてメニューを開く　「ヘルプ」をクリック
		webDriver.findElement(By.linkText("機能")).click();
		webDriver.findElement(By.linkText("ヘルプ")).click();

		// ヘルプ画面に遷移したことをURLで確認
		new WebDriverWait(webDriver, Duration.ofSeconds(5)).until(driver -> driver.getCurrentUrl().contains("/help"));
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {

		// 現在のタブを保存
		String originalWindow = webDriver.getWindowHandle();

		// 「よくある質問」をクリック
		webDriver.findElement(By.linkText("よくある質問")).click();

		// 新しいタブが開くまで待つ
		new WebDriverWait(webDriver, Duration.ofSeconds(5)).until(driver -> driver.getWindowHandles().size() > 1);

		// 新しいタブへ切り替える
		for (String windowHandle : webDriver.getWindowHandles()) {
			if (!windowHandle.equals(originalWindow)) {
				webDriver.switchTo().window(windowHandle);
				break;
			}
		}
		// FAQページであることをURLで確認
		new WebDriverWait(webDriver, Duration.ofSeconds(5))
				.until(driver -> driver.getCurrentUrl().contains("/faq"));
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 キーワード検索で該当キーワードを含む検索結果だけ表示")
	void test05() {

		// 検索のキーワードを入力
		String keyword = "研修";
		webDriver.findElement(By.id("form")).sendKeys(keyword);
		//		検索したいkeywordを入力した後　クリアボタンを押下する
		webDriver.findElement(By.cssSelector("input[type='submit'][value='検索']")).click();

		// 検索した結果を　questions　に取得する
		var questions = webDriver.findElements(By.cssSelector("dl[id^='question-h'] dt"));

		// 検索した結果は1行以上あるかどうかを確認
		assertTrue(questions.size() > 0);

		// 一行づつkeyword　の値を確認
		for (var question : questions) {
			System.out.println("検索結果：" + question.getText());
			assertTrue(question.getText().contains(keyword));
		}
		// エビデンス
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 「クリア」ボタン押下で入力したキーワードを消去")
	void test06() {

		// クリア　ボタンをクリック 
		webDriver.findElement(By.cssSelector("input[type='button'][value='クリア']")).click();

		// キーワード入力の確認 
		assertEquals("", webDriver.findElement(By.id("form")).getAttribute("value"));
		getEvidence(new Object() {
		});
	}
}
