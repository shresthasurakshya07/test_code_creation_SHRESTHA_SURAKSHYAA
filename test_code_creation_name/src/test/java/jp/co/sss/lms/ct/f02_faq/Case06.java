package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.*;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト よくある質問機能
 * ケース06
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

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

		// エビデンス
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

		// エビデンス
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

		// FAQ画面であることを検証
		assertEquals("よくある質問", webDriver.findElement(By.xpath("//h2[text()='よくある質問']")).getText());

		// エビデンス
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() {

		// 「研修関係」をクリック
		webDriver.findElement(By.linkText("【研修関係】")).click();

		// 検索結果の質問を取得
		List<WebElement> question = webDriver.findElements(
				By.cssSelector("dl[id^='question-h'] dt"));

		// 検索結果が2件であることを検証
		assertEquals(2, question.size());

		// 1件目の検索結果を検証
		assertEquals("Q.キャンセル料・途中退校について", question.get(0).getText());

		// 2件目の検索結果を検証
		assertEquals("Q.研修の申し込みはどのようにすれば良いですか？", question.get(1).getText());

		// 検索結果を見える位置までスクロール
		((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0, document.body.scrollHeight);");

		// エビデンス
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() {

		// 「研修の申し込みはどのようにすれば良いですか？」を取得
		WebElement question = webDriver.findElement(
				By.xpath("//dt[contains(.,'研修の申し込みはどのようにすれば良いですか？')]"));

		// 質問をクリック
		question.click();

		// クリックした質問の回答を取得
		WebElement answer = question.findElement(By.xpath("./following-sibling::dd"));

		// 回答が表示される位置までスクロール
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block:'center'});", answer);

		// 回答が表示されていることを検証
		assertTrue(answer.isDisplayed());

		// 回答のclassが「dn」ではないことを検証
		assertFalse(answer.getAttribute("class").contains("dn"));

		// エビデンス
		getEvidence(new Object() {
		});
	}
}