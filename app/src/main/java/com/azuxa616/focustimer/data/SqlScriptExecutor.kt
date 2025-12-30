package com.azuxa616.focustimer.data

import android.content.Context
import android.util.Log
import androidx.sqlite.db.SupportSQLiteDatabase
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * SQL脚本执行器
 * 
 * 用于从assets目录读取SQL文件并执行
 */
object SqlScriptExecutor {
    private const val TAG = "SqlScriptExecutor"

    /**
     * 执行SQL脚本文件
     * 
     * @param context 上下文，用于访问assets
     * @param database SQLite数据库实例
     * @param scriptFileName assets目录中的SQL文件名
     * @return 执行结果，成功返回true，失败返回false并记录错误日志
     */
    fun executeScript(
        context: Context,
        database: SupportSQLiteDatabase,
        scriptFileName: String
    ): Result<String> {
        return try {
            // 读取SQL文件内容
            val sqlContent = readSqlFile(context, scriptFileName)
            
            // 解析SQL语句
            val statements = parseSqlStatements(sqlContent)
            
            if (statements.isEmpty()) {
                return Result.failure(Exception("SQL脚本中没有有效的SQL语句"))
            }
            
            // 在事务中执行所有SQL语句
            database.beginTransaction()
            try {
                statements.forEach { statement ->
                    if (statement.isNotBlank()) {
                        database.execSQL(statement)
                        Log.d(TAG, "执行SQL: ${statement.take(50)}...")
                    }
                }
                database.setTransactionSuccessful()
                val message = "成功执行 ${statements.size} 条SQL语句"
                Log.i(TAG, message)
                Result.success(message)
            } catch (e: Exception) {
                Log.e(TAG, "执行SQL时出错", e)
                Result.failure(e)
            } finally {
                database.endTransaction()
            }
        } catch (e: Exception) {
            Log.e(TAG, "读取或执行SQL脚本失败", e)
            Result.failure(e)
        }
    }

    /**
     * 从assets目录读取SQL文件
     */
    private fun readSqlFile(context: Context, fileName: String): String {
        context.assets.open(fileName).use { inputStream ->
            BufferedReader(InputStreamReader(inputStream, "UTF-8")).use { reader ->
                return reader.readText()
            }
        }
    }

    /**
     * 解析SQL内容，提取有效的SQL语句
     * 
     * 处理规则：
     * 1. 过滤注释行（以--开头）
     * 2. 按分号分割SQL语句
     * 3. 去除空白字符
     */
    private fun parseSqlStatements(sqlContent: String): List<String> {
        val statements = mutableListOf<String>()
        val lines = sqlContent.lines()
        val currentStatement = StringBuilder()
        
        for (line in lines) {
            val trimmedLine = line.trim()
            
            // 跳过空行和注释行
            if (trimmedLine.isEmpty() || trimmedLine.startsWith("--")) {
                continue
            }
            
            // 添加到当前语句
            currentStatement.append(trimmedLine).append(" ")
            
            // 如果行以分号结尾，说明语句结束
            if (trimmedLine.endsWith(";")) {
                val statement = currentStatement.toString()
                    .trim()
                    .removeSuffix(";")
                    .trim()
                
                if (statement.isNotBlank()) {
                    statements.add(statement)
                }
                currentStatement.clear()
            }
        }
        
        // 处理最后一条语句（如果没有以分号结尾）
        val lastStatement = currentStatement.toString().trim()
        if (lastStatement.isNotBlank() && !lastStatement.endsWith(";")) {
            statements.add(lastStatement)
        }
        
        return statements
    }
}

