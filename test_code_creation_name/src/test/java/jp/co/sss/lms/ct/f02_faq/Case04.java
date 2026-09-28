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
 * ケース04
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース04 よくある質問画面への遷移")
public class Case04 {

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
		// トップページURLにアクセス
		goTo("http://localhost:8080/lms");
		// ログイン画面が表示されるまで待つ
		visibilityTimeout(By.xpath("//h2[text()='ログイン']"), 5);
		// ログイン画面であることを検証
		assertEquals("ログイン", webDriver.findElement(By.xpath("//h2[text()='ログイン']")).getText());
		// エビデンス
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {

		// ログインIDを入力
		webDriver.findElement(By.id("loginId")).sendKeys("StudentAA02");

		// パスワードを入力
		webDriver.findElement(By.id("password")).sendKeys("StudentAA021");

		// ログイン
		webDriver.findElement(By.cssSelector("input[type='submit'][value='ログイン']")).click();

		// コース詳細画面であることを検証
		assertEquals("コース詳細", webDriver.findElement(By.cssSelector("ol.breadcrumb li.active")).getText());
		// エビデンス取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {

		// 「機能」をクリックしてメニューを開く
		webDriver.findElement(By.linkText("機能")).click();

		// 「ヘルプ」をクリック
		webDriver.findElement(By.linkText("ヘルプ")).click();

		// ヘルプ画面に遷移するまで待つ
		new WebDriverWait(webDriver, Duration.ofSeconds(5)).until(driver -> driver.getCurrentUrl().contains("/help"));

		// ヘルプ画面であることを検証
		assertEquals("ヘルプ", webDriver.findElement(By.xpath("//h2[text()='ヘルプ']")).getText());

		// エビデンス取得
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
		// FAQページに遷移するまで待つ
		new WebDriverWait(webDriver, Duration.ofSeconds(5)).until(driver -> driver.getCurrentUrl().contains("/faq"));

		// よくある質問画面であることを検証
		assertEquals("よくある質問", webDriver.findElement(By.xpath("//h2[text()='よくある質問']")).getText());

		// エビデンス取得
		getEvidence(new Object() {
		});
	}
}