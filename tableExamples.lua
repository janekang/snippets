--[[
Demonstrates initializing procedures of Lua.

Jane Kang, Agent Developer Intern
]]--

a = {} -- Empty table
b = {x = 0, y = 0} -- Used like dict
c = {["x"] = 0, ["y"] = 0}
print (b["x"] == c["x"])


d = {"red", "green", "blue"} -- Used like list
e = {[1] = "red", [2] = "green", [3] = "blue"} -- Index starts at 1, not 0
print(d[1] == e[1])

-- For index starting at 0
f = {[0] = "red", "green", "blue"}

-- ; in place of ,
g = {x = 10, y = 47; "red", "green", "blue"}
print(g[1] == "red")
