package com.kampplus.hava

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kampplus.hava.ui.theme.HavaTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.kampplus.hava.ui.city.CityRoute

/**
 * Uygulamanın tek Activity'si; kullanıcının girdiği kapı.
 *
 * Activity bir **pencereyi** yönetir. Pencerenin içine ne çizileceğini ise
 * Compose söyler: sahne Activity'dir, dekoru Compose kurar.
 */
class MainActivity : ComponentActivity() {

    /**
     * Ekran oluşturulurken Android bu fonksiyonu **kendisi** çağırır; biz çağırmayız.
     * Yaşam döngüsünün ilk adımıdır.
     *
     * Logcat'i açıp uygulamayı arka plana alıp geri dönerek `onCreate` / `onStart` /
     * `onResume` sırasını gözlemleyebilirsin.
     */
    // 1. İŞÇİ (Alt Fonksiyon): Sadece tıklamayı haber verir ve verilen sayıyı yazar
    @Composable
    fun SayacButonu(count: Int, onClick: () -> Unit) {
        Button(onClick = onClick) { // Tıklanınca yukarıdaki 'onClick' çalışır
            Text(text = "Sayaç: $count - Artır")
        }
    }
    @Composable
    fun Deney3Ekran(viewModel: CounterViewModel = viewModel()) {
        // ViewModel'deki sayacı CANLI OARAK dinliyoruz (observe ediyoruz):
        val count by viewModel.count.collectAsStateWithLifecycle()

        Column(modifier = Modifier.padding(16.dp)) {
            SayacButonu(
                count = count,
                onClick = { viewModel.increase() } // Tıklanınca ViewModel'deki fonksiyonu çağırıyoruz
            )
        }
    }

    // 2. PATRON (Üst Fonksiyon): Hafızayı tutar, haberi alınca 'count++' yapar
    @Composable
    fun Deney2Ekran() {
        var count by rememberSaveable { mutableStateOf(0) } // Hafıza burada

        Column(modifier = Modifier.padding(16.dp)) {
            SayacButonu(
                count = count,       // Sayıyı İşçiye verir
                onClick = { count++ } // İşçiden haber gelince sayıyı 1 artırır
            )
        }
    }

    @Composable
    fun Deney1Ekran() {

        var count by rememberSaveable() { mutableStateOf(0) }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Sayaç: $count")
            Button(onClick = { count++ }) {
                Text(text = "Artır")
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        // İçeriği durum ve navigasyon çubuklarının altına kadar yayar.
        enableEdgeToEdge()

        // Süslü parantezin içi Compose dünyasıdır.
        setContent {
            HavaTheme {
                CityRoute()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    private companion object {
        /** Logcat'te filtrelemek için: arama kutusuna `Lifecycle` yaz. */
        const val TAG = "Lifecycle"
    }
}
class CounterViewModel : ViewModel() {
    private val state = MutableStateFlow(0) // Veriyi gizli tutarız
    val count: StateFlow<Int> = state.asStateFlow() // Dışarıya okunabilir sunarız

    // Butona basılınca sayıyı 1 artıran fonksiyon:
    fun increase() {
        state.update { it + 1 }
    }
}
