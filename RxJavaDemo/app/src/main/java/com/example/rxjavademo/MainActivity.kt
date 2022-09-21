package com.example.rxjavademo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.functions.Supplier
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import org.reactivestreams.Subscription
import java.util.*
import java.util.concurrent.Flow
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val TAG = "===>"
    private var subscription: Subscription? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn)
            .setOnClickListener {
                subscription?.request(64)
            }
//        testMaybe()
        testThrottle()
    }

    private fun testDisposable() {
        var disposable = Observable.create { emitter ->
            (0..3).forEach {
                emitter.onNext(it)
            }
        }
            .map { it.toString() }
            .subscribe { Log.d(TAG, it) }
    }

    private fun testCompletable() {
        Completable.create(object : CompletableOnSubscribe {
            override fun subscribe(emitter: CompletableEmitter) {
                TimeUnit.SECONDS.sleep(1)
                Log.i(TAG, "执行完onComplete")
                emitter.onComplete()
            }
        }).andThen(Observable.range(1, 10))
            .subscribe(object : Observer<Int> {
                override fun onSubscribe(d: Disposable) {
                    Log.d(TAG, "onSubscribe")
                }

                override fun onNext(t: Int) {
                    Log.d(TAG, "onNext:$t")
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, "onError:$e")
                }

                override fun onComplete() {
                    Log.d(TAG, "onComplete")
                }

            })
    }

    private fun testSingle() {
        Single.create {
            it.onSuccess(10)
            it.onSuccess(20)  //这个不会发给下游
        }.subscribe(object : SingleObserver<Int> {
            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, "onSubscribe")
            }

            override fun onSuccess(t: Int) {
                Log.d(TAG, "onSuccess:$t")
            }

            override fun onError(e: Throwable) {
                Log.e(TAG, "onError:$e")
            }
        })
    }

    private fun testMaybe() {
        Maybe.create(object : MaybeOnSubscribe<Int> {
            override fun subscribe(emitter: MaybeEmitter<Int>) {
//                emitter.onSuccess(10)
//                emitter.onSuccess(20)
                emitter.onComplete()
            }
        }).subscribe(object : MaybeObserver<Int> {
            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, "onSubscribe")
            }

            override fun onError(e: Throwable) {
                Log.e(TAG, "onError:$e")
            }

            override fun onComplete() {
                Log.d(TAG, "onComplete")
            }

            override fun onSuccess(t: Int) {
                Log.d(TAG, "onSuccess:$t")
            }

        })
    }

    private fun testFlowable() {
        PublishProcessor.create<Int>()

        Flowable.create<Int>({ emitter ->
            Log.d(TAG, "requested:${emitter.requested()}")
            (0..1000).forEach {
                emitter.onNext(it)
            }
        }, BackpressureStrategy.LATEST)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : FlowableSubscriber<Int> {
                override fun onSubscribe(s: Subscription) {
                    Log.i(TAG, "onSubscribe")
                    subscription = s
                }

                override fun onNext(t: Int) {
                    Log.i(TAG, "onNext：$t")
                }

                override fun onError(t: Throwable?) {
                    Log.e(TAG, "onError：$t")
                }

                override fun onComplete() {
                    Log.i(TAG, "onComplete")
                }
            })
    }

    private fun testDo() {
        Observable.range(1, 5)
            .doOnEach { integerNotification: Notification<Int> ->
                Log.d(
                    TAG,
                    "Each : " + integerNotification.value
                )
            }
            .doOnComplete { Log.d(TAG, "complete") }
            .doFinally { Log.d(TAG, "finally") }
            .doAfterNext { i: Int -> Log.d(TAG, "after next : $i") }
            .doOnSubscribe { disposable: Disposable? ->
                Log.d(
                    TAG,
                    "subscribe"
                )
            }
            .doOnTerminate { Log.d(TAG, "terminal") }
            .subscribe { i: Int -> Log.d(TAG, "subscribe : $i") }
    }

    private fun testZip() {
//        Observable.zip(Observable.range(1, 6), Observable.range(6, 5)) { i1, i2 -> i1 * i2 }
//            .subscribe { Log.d(TAG, it.toString()) }
        Observable.combineLatest(Observable.range(1, 6), Observable.range(6, 5)) { i1, i2 ->
            Log.i(TAG, "i1:$i1,i2:$i2")
            i1 * i2
        }
            .subscribe { Log.d(TAG, it.toString()) }
    }

    private fun testMerge() {
        Observable.merge(
            Observable.range(1, 5),
            Observable.range(10, 5)
        )
            .subscribe { Log.d(TAG, it.toString()) }
    }

    private fun testThrottle() {
        Observable.interval(80, TimeUnit.MILLISECONDS)
            .throttleLatest(500, TimeUnit.MILLISECONDS)
            .subscribe { Log.d(TAG, it.toString()) }

//        Observable.create { emitter ->
//            (0..10).forEach {
//                if (!emitter.isDisposed) {
//                    emitter.onNext(it)
//                }
//                val sleep = if (it % 3 == 0) 300 else 100
//                Thread.sleep(sleep.toLong())
//            }
//            emitter.onComplete()
//        }.subscribeOn(Schedulers.computation())
//            .throttleWithTimeout(200, TimeUnit.MILLISECONDS)
//            .subscribe { Log.d(TAG, it.toString()) }
    }

    private fun testWindow() {
        Observable.range(1, 10)
            .window(3)
            .subscribe { observable ->
                Log.i(TAG, observable.hashCode().toString())
                observable.subscribe { Log.d(TAG, "observable:${observable.hashCode()}，value:$it") }
            }
    }

    private fun testScan() {
        Observable.range(2, 5).scan { i1, i2 ->
            Log.i(TAG, "i1:$i1,i2:$i2")
            i1 * i2
        }.subscribe { Log.d(TAG, it.toString()) }
    }

    private fun testGroupBy() {
        Observable.just(
            "John", "Steve", "Ruth",
            "Sam", "Jane", "James"
        )
            .groupBy { it[0] }
            .subscribe { grouped ->
                Log.i(TAG, "key:${grouped.key}")
                grouped.subscribe {
                    Log.d(
                        TAG,
                        "key:${grouped.key},value:$it, groupedObservable:${grouped.hashCode()}"
                    )
                }
            }
    }

    private fun testBuffer() {
        Observable.range(1, 7).buffer(3)
            .subscribe { Log.d(TAG, it.toString()) }
    }

    private fun testFlatMapIterable() {
        Observable.range(1, 5)
            .flatMapIterable { listOf<Int>(it, it * 10) }
            .subscribe { Log.d(TAG, "flatMapIterable:$it") }
    }

    private fun testFlatMap() {
        Observable.range(1, 10)
            .flatMap { a ->
                Observable.just(a).delay((11 - a).toLong(), TimeUnit.SECONDS)
            }
            .subscribe { Log.d(TAG, "flatmap:$it") }

        Observable.range(1, 10)
            .concatMap { a ->
                Observable.just(a).delay((11 - a).toLong(), TimeUnit.SECONDS)
            }
            .subscribe { Log.d(TAG, "concatMap:$it") }
    }

    private fun testCast() {
        Observable.just(Date()).cast(Object::class.java)
            .subscribe { Log.d(TAG, "===>${it.toString()}") }
    }

    private fun testInterval() {
        Observable.interval(3, TimeUnit.SECONDS).subscribe { Log.e("TAG", it.toString()) }
    }

    private fun testRange() {
        Observable.range(5, 10).subscribe { Log.e("TAG", it.toString()) }
    }

    private fun testDefer() {
//        val observable = Observable.just(System.currentTimeMillis())
        val observable = Observable.defer(Supplier { Observable.just(System.currentTimeMillis()) })
        observable.subscribe { Log.e(TAG, "第一次subscribe：$it") }
        observable.subscribe { Log.e(TAG, "第二次subscribe：$it") }
    }

    private fun testEmptyAndNeverAndError() {
        Observable.empty<String>()
            .subscribe(
                { Log.e(TAG, "empty onNext") },
                { Log.e(TAG, "empty onError") },
                { Log.e(TAG, "empty onComplete") },
            )
        Observable.never<String>()
            .subscribe(
                { Log.e(TAG, "never onNext") },
                { Log.e(TAG, "never onError") },
                { Log.e(TAG, "never onComplete") },
            )
        Observable.error<String>(Exception())
            .subscribe(
                { Log.e(TAG, "error onNext") },
                { Log.e(TAG, "error onError") },
                { Log.e(TAG, "error onComplete") },
            )
    }

    private fun testRepeat() {
        Observable.range(1, 5)
            .repeatWhen { Observable.timer(6, TimeUnit.SECONDS) }
            .subscribe { Log.e("TAG", it.toString()) }
    }

    /**
     * 线程切换
     */
    private fun testScheduler() {
        Log.e("TAG", "test(): " + Thread.currentThread().name)
        Observable.create(ObservableOnSubscribe<String> { emitter ->
            Log.e("TAG", "subscribe(): " + Thread.currentThread().name)
            emitter.onNext("1")
            emitter.onNext("2")
            emitter.onComplete()
        }).subscribeOn(Schedulers.io())
            .map {
                Log.i("TAG", "map()" + Thread.currentThread().name)
                it
            }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<String> {
                override fun onComplete() {
                    Log.e("TAG", "onComplete(): " + Thread.currentThread().name)
                }

                override fun onSubscribe(d: Disposable) {
                    Log.e("TAG", "onSubscribe(): " + Thread.currentThread().name)
                }

                override fun onNext(t: String) {
                    Log.e("TAG", "onNext(): " + Thread.currentThread().name)
                }

                override fun onError(e: Throwable) {
                    Log.e("TAG", "onError(): " + Thread.currentThread().name)
                }
            })
    }
}